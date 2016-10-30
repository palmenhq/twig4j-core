package org.twig.syntax.parser;

import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.operator.Operator;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.*;

import java.util.ArrayList;
import java.util.HashMap;

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
    public Node parseExpression() throws SyntaxErrorException, TwigRuntimeException {
        return parseExpression(0);
    }

    /**
     * Parse an expression
     *
     * @param precedence TODO find out what this thing is
     * @return The node for the parsed expression
     */
    public Node parseExpression(Integer precedence) throws SyntaxErrorException, TwigRuntimeException {
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
        // TODO check if unary or is opening parenthesis
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
                switch (token.getValue()) {
                    case "true":
                    case "TRUE":
                        node = new Constant("true", token.getLine());
                        break;
                    case "false":
                    case "FALSE":
                        node = new Constant("false", token.getLine());
                        break;
                    case "null":
                    case "NULL":
                    case "none":
                    case "NONE":
                        node = new Constant("null", token.getLine());
                        break;
                    default:
                        // TODO check for function

                        node = new Name(token.getValue(), token.getLine());
                }
                break;

            case NUMBER:
                parser.getTokenStream().next();
                node = new Constant(token.getValue(), token.getLine());
                break;

            case STRING:
            case INTERPLATION_START:
                node = parseStringExpression();
                break;

            default:
                throw SyntaxErrorException.unexpectedToken(token, parser.getFilename(), token.getLine());
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
                nodes.add(new Constant(token.getValue(), token.getLine()));
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
                node = parseSubscripExpression(node);
            } else if (false) {
                // TODO filters
            } else {
                break;
            }
        }

        return node;
    }

    /**
     *
     * @param node
     * @return
     * @throws SyntaxErrorException
     */
    public Expression parseSubscripExpression(Expression node) throws SyntaxErrorException, TwigRuntimeException {
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
                arg = new Constant(token.getValue(), token.getLine());

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
}
