package org.twig.syntax.parser;

import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.operator.Operator;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.*;

import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;

/**
 * Parses expressions.
 *
 * This parser implements a "Precedence climbing" algorithm.
 *
 * @see http://www.engr.mun.ca/~theo/Misc/exp_parsing.htm
 * @see http://en.wikipedia.org/wiki/Operator-precedence_parser
 */
public class ExpressionParser {
    private Parser parser;

    public ExpressionParser(Parser parser) {
        this.parser = parser;
    }

    /**
     * @see ExpressionParser#parseExpression(Integer) - defaults to 0
     */
    public Expression parseExpression() throws SyntaxErrorException, TwigRuntimeException {
        return parseExpression(0);
    }

    /**
     * Parse an expression
     *
     * @param precedence TODO find out what this thing is
     * @return The node for the parsed expression
     */
    public Expression parseExpression(Integer precedence) throws SyntaxErrorException, TwigRuntimeException {
        Expression expr = getPrimary();
        Token token = parser.getCurrentToken();

        while(isBinary(token.getValue()) && getBinaryOperator(token.getValue()).getPrecedence() >= precedence) {
            Token operatorToken = token;
            Operator operator = getBinaryOperator(token.getValue());
            parser.getTokenStream().next();

            // TODO find out what the "callable" thing is for binary operators
            Node expr2 = parseExpression(
                    operator.getAssociativity() == Operator.Associativity.LEFT
                            ? (operator.getPrecedence() + 1) : operator.getPrecedence()
            );
            Class nodeClass = operator.getNodeClass();

            try {
                expr = (Expression)nodeClass
                        .getConstructor(Node.class, Node.class, Integer.class)
                        .newInstance(expr, expr2, token.getLine());
            } catch (Exception e) {
                throw TwigRuntimeException.badOperatorFailedNode(operatorToken.getValue(), parser.getFilename(), operatorToken.getLine(), e);
            }

            token = parser.getCurrentToken();
        }

        return parsePostfixExpression(expr);
    }

    /**
     * TODO find a good doscription of this method
     *
     * @return The node for this expression
     * @throws SyntaxErrorException On syntax errors
     */
    protected Expression getPrimary() throws SyntaxErrorException, TwigRuntimeException {
        Token token = parser.getCurrentToken();

        if (isUnary(token.getValue())) {
            Operator operator = getUnaryOperator(token.getValue());
            parser.getTokenStream().next();
            Expression expression = parseExpression(operator.getPrecedence());

            try {
                Constructor operatorConstructor = operator.getNodeClass().getConstructor(Node.class, Integer.class);
                return parsePostfixExpression((Expression)operatorConstructor.newInstance(expression, token.getLine()));
            } catch (Exception e) {
                throw new TwigRuntimeException(
                        "Missing or misconfigured constructor or class for operator \"" + operator.getNodeClass().getName() + "\"",
                        parser.getFilename(),
                        token.getLine(),
                        e
                );
            }
        }

        if (token.is(Token.Type.PUNCTUATION, "(")) {
            parser.getTokenStream().next();
            Expression expression = parseExpression();
            parser.getTokenStream().expect(Token.Type.PUNCTUATION, ")");

            return parsePostfixExpression(expression);
        }
        return parsePrimaryExpression();
    }

    /**
     * TODO find a good description of this method
     *
     * @return A node for the expression
     * @throws SyntaxErrorException On syntax errors
     */
    public Expression parsePrimaryExpression() throws SyntaxErrorException, TwigRuntimeException {
        Token token = parser.getCurrentToken();
        Expression node;

        switch(token.getType()) {
            case NAME:
                parser.getTokenStream().next();

                if (getScalarValue(token) == null || !getScalarValue(token).getClass().equals(String.class)) {
                    node = new Constant(getScalarValue(token), token.getLine());
                } else {
                    node = new Name(token.getValue(), token.getLine());
                }

                break;

            case NUMBER:
                parser.getTokenStream().next();
                Object scalarValue = getScalarValue(token);
                if (scalarValue.getClass().equals(Integer.class) || scalarValue.getClass().equals(Double.class)) {
                    node = new Constant(scalarValue, token.getLine());
                } else {
                    throw new TwigRuntimeException("Error parsing number of value \"" + token.getValue() + "\"", parser.getFilename(), token.getLine());
                }
                break;

            case STRING:
            case INTERPLATION_START:
                node = parseStringExpression();
                break;

            case OPERATOR:
                // TODO find out what this does and if it's correctly implemented here - not covered by tests right now
                Matcher nameMatcher = parser.getEnvironment().getLexer().getRegexes().getExpressionName().matcher(token.getValue());
                if (nameMatcher.find() && nameMatcher.group(0).equals(token.getValue())) {
                    // in this context, string operators are variable names
                    parser.getTokenStream().next();
                    node = new Name(token.getValue(), token.getLine());
                } else if (isUnary(token.getValue())) {
                    Class operatorClass = getUnaryOperator(token.getValue()).getNodeClass();
                    // TODO neg/pos unary operators check thing
                    throw new TwigRuntimeException("Pos and neg operators are not yet implemented in this version of twig", parser.getFilename(), token.getLine());
                } else {
                    throw new TwigRuntimeException("An unknown operator was passed to the expression parser", parser.getFilename(), token.getLine());
                }
                break;

            default:
                if (token.is(Token.Type.PUNCTUATION, "[")) {
                    node = parseArrayExpression();
                } else if (token.is(Token.Type.PUNCTUATION, "{")) {
                    node = parseHashExpression();
                } else {
                    throw SyntaxErrorException.unexpectedToken(token, parser.getFilename(), token.getLine());
                }
        }

        return node;
    }

    /**
     * Parse a string expression (a regular string or a string interpolation)
     *
     * @return The nodes that represents the string expression
     * @throws SyntaxErrorException
     */
    public Expression parseStringExpression() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream stream = parser.getTokenStream();
        ArrayList<Node> nodes = new ArrayList<>();

        Boolean nextCanBeString = true;

        while (true) {
            if (nextCanBeString && stream.getCurrent().is(Token.Type.STRING)) {
                Token token = stream.next();
                nodes.add(new StringConstant(token.getValue(), token.getLine()));
                nextCanBeString = false;
            } else if (stream.getCurrent().is(Token.Type.INTERPLATION_START)) {
                stream.next();
                nodes.add(parseExpression());
                stream.expect(Token.Type.INTERPOLATION_END);
                nextCanBeString = true;
            } else {
                break;
            }
        }

        // Add a concat for each node in the list if more than one
        Expression expression = (Expression)nodes.remove(0);
        for (Node node : nodes) {
            expression = new BinaryConcat(expression, node, node.getLine());
        }

        return expression;
    }

    /**
     * Parse an array
     *
     * @return The array expression
     * @throws SyntaxErrorException If not followed by comma or if not closed
     * @throws TwigRuntimeException
     */
    public Expression parseArrayExpression() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = parser.getTokenStream();
        tokenStream.expect(Token.Type.PUNCTUATION, "[", "An array element was expected");

        Expression array = new Array(tokenStream.getCurrent().getLine());
        boolean first = true;
        while (!tokenStream.getCurrent().is(Token.Type.PUNCTUATION, "]")) {
            if (!first) {
                tokenStream.expect(Token.Type.PUNCTUATION, ",", "An array element must be followed by a comma");

                // trailing ,?
                if (tokenStream.getCurrent().is(Token.Type.PUNCTUATION, ",")) {
                    break;
                }

            }
            first = false;

            array.addNode(parseExpression());
        }

        tokenStream.expect(Token.Type.PUNCTUATION, "]", "An opened array is not properly closed");

        return array;
    }

    /**
     * Parse a hash expression (hashmap, ie {foo: bar})
     * @return The hash expression
     *
     * @throws SyntaxErrorException
     * @throws TwigRuntimeException
     */
    public Expression parseHashExpression() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = parser.getTokenStream();
        tokenStream.expect(Token.Type.PUNCTUATION, "{", "A hash element was expected");

        Expression hash = new Hash(tokenStream.getCurrent().getLine());
        boolean first = true;
        while (!tokenStream.getCurrent().is(Token.Type.PUNCTUATION, "}")) {
            if (!first) {
                tokenStream.expect(Token.Type.PUNCTUATION, ",", "A hash value must be followed by a comma");

                // Trailing ,?
                if (tokenStream.getCurrent().is(Token.Type.PUNCTUATION, "}")) {
                    break;
                }
            }
            first = false;

            /* a hash key can be:
             *
             *  * a number -- 12
             *  * a string -- 'a'
             *  * a name, which is equivalent to a string -- a
             *  * an expression, which must be enclosed in parentheses -- (1 + 2)
             */
            String key;
            if (tokenStream.getCurrent().is(Token.Type.STRING) || tokenStream.getCurrent().is(Token.Type.NUMBER) || tokenStream.getCurrent().is(Token.Type.NAME)) {
                key = tokenStream.getCurrent().getValue();
                tokenStream.next();
                // TODO if "("
            } else {
                throw new SyntaxErrorException(
                        String.format(
                                "A hash key must be a quoted string, a number, a name, or an expression enclosed in parentheses (unexpected token \"%s\" of value \"%s\"",
                                Token.typeToEnglish(tokenStream.getCurrent().getType()),
                                tokenStream.getCurrent().getValue()
                        ),
                        parser.getFilename(),
                        tokenStream.getCurrent().getLine()
                );
            }

            tokenStream.expect(Token.Type.PUNCTUATION, ":", "A hash key must be followed by a colon (:)");
            Expression value = parseExpression();
            hash.putAttribute(key, value);
        }
        tokenStream.expect(Token.Type.PUNCTUATION, "}", "An opened hash is not properly closed");

        return hash;
    }

    /**
     * Parse something that comes after a NAME
     *
     * @param node The node that's has something post it
     * @return
     * @throws SyntaxErrorException
     */
    public Expression parsePostfixExpression(Expression node) throws SyntaxErrorException, TwigRuntimeException {
        while (true) {
            Token token = parser.getCurrentToken();

            if (token.getType() != Token.Type.PUNCTUATION) {
                break;
            }

            if (token.getValue().equals(".") || token.getValue().equals("[")) {
                node = parseSubscriptExpression(node);
            } else if (false) {
                // TODO filters
            } else {
                break;
            }
        }

        return node;
    }

    /**
     * Parse if this is a method/function call or fetching something from an array
     *
     * @param node
     * @return
     * @throws SyntaxErrorException
     */
    public Expression parseSubscriptExpression(Expression node) throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = parser.getTokenStream();
        Token token = tokenStream.next();
        Array arguments = new Array(token.getLine());
        String type = "any";
        Expression arg = null;

        if (token.getValue().equals(".")) {
            token = tokenStream.next();
            if (
                    token.getType() == Token.Type.NAME
                    || token.getType() == Token.Type.NUMBER
                    || token.getType() == Token.Type.OPERATOR // TODO get if matches name operator
            ) {
                arg = new Constant(getScalarValue(token), token.getLine());

                // If this is a method
                if (parser.getCurrentToken().is(Token.Type.PUNCTUATION, "(")) {
                    type = "method";

                    // Add all nodes to the argument
                    parseArguments().getNodes().forEach(arguments::addNode);
                }
            } else {
                throw new SyntaxErrorException("Expected name or number", tokenStream.getFilename(), token.getLine());
            }

            // TODO check if this is a macro
        } else {
            type = "array";
            // TODO slice (when we have the filter)
            arg = parseExpression();

            tokenStream.expect(Token.Type.PUNCTUATION, "]");
        }

        return new GetAttr(node, arg, arguments, type, token.getLine());
    }

    /**
     * @see static#parseArguments(boolean, boolean)
     * Defaults both parameters to false
     *
     * @return The argument nodes
     * @throws SyntaxErrorException
     */
    public Node parseArguments() throws SyntaxErrorException, TwigRuntimeException {
        return parseArguments(false, false);
    }

    /**
     * @see static#parseArguments(boolean, boolean)
     * Defaults isFunctionDefinition to false
     *
     * @param useNamedArguments Whether to use named arguments
     * @return The argument nodes
     * @throws SyntaxErrorException
     */
    public Node parseArguments(boolean useNamedArguments) throws SyntaxErrorException, TwigRuntimeException {
        return parseArguments(useNamedArguments, false);
    }

    /**
     * Parse function/method arguments
     * @param useNamedArguments Whether to use named arguments
     * @param isFunctionDefinition Whether these arguments are for a function definition or a call
     * @return
     * @throws SyntaxErrorException
     */
    public Node parseArguments(boolean useNamedArguments, boolean isFunctionDefinition) throws SyntaxErrorException, TwigRuntimeException {
        ArrayList<Node> arguments = new ArrayList<>();
        TokenStream stream = parser.getTokenStream();

        stream.expect(Token.Type.PUNCTUATION, "(", "A list of arguments must begin with an opening parenthesis");

        while (!stream.getCurrent().is(Token.Type.PUNCTUATION, ")")) {
            if (arguments.size() != 0) {
                stream.expect(Token.Type.PUNCTUATION, ",", "Arguments must be separated by a comma");
            }

            // TODO check for definition
            Node value = parseExpression();

            // TODO check for named arguments

            // TODO something about the isFunctionDefinition and something about useNamedArguments
            arguments.add(value);

        }

        stream.expect(Token.Type.PUNCTUATION, ")", "A list of arguments must be closed by a parenthesis");

        return new Node(arguments, new HashMap<>(), -1, stream.getFilename());
    }

    /**
     * Check whether the operator is in the binary operators map in the env
     * @param operator The operator to check for
     * @return
     */
    public boolean isBinary(String operator) {
        return this.parser.getEnvironment().getBinaryOperators().containsKey(operator);
    }

    /**
     * Get an operator from the twig environment by the name
     * @param operator The operator name
     * @return
     */
    public Operator getBinaryOperator(String operator) {
        return this.parser.getEnvironment().getBinaryOperators().get(operator);
    }

    /**
     * Check whether the operator is in the unary operators map in the env
     * @param operator The operator to check for
     * @return
     */
    public boolean isUnary(String operator) {
        return this.parser.getEnvironment().getUnaryOperators().containsKey(operator);
    }

    /**
     * Get an operator from the twig environment by the name
     * @param operator The operator name
     * @return
     */
    public Operator getUnaryOperator(String operator) {
        return this.parser.getEnvironment().getUnaryOperators().get(operator);
    }

    /**
     * Get a scalar value, i.e. true, 1 or "foo"
     *
     * @param token The token to get the value from
     * @return
     */
    protected Object getScalarValue(Token token) {
        switch (token.getValue()) {
            case "true":
            case "TRUE":
                return true;
            case "false":
            case "FALSE":
                return false;
            case "null":
            case "NULL":
            case "none":
            case "NONE":
                return null;
            default:
                // TODO check for function

                if (token.getValue().matches("^(\\d+)$")) {
                    return (Integer)Integer.parseInt(token.getValue());
                } else if (token.getValue().matches("^[\\d\\.]+$")) {
                    return (Double)Double.parseDouble(token.getValue());
                }

                return token.getValue();
        }
    }
}
