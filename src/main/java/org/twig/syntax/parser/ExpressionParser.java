package org.twig.syntax.parser;

import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.operator.Operator;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.BinaryConcat;
import org.twig.syntax.parser.node.type.expression.Constant;
import org.twig.syntax.parser.node.type.expression.Name;

import java.util.ArrayList;

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
        Node expr = getPrimary();
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
                expr = (Node)nodeClass
                        .getConstructor(Node.class, Node.class, Integer.class)
                        .newInstance(expr, expr2, token.getLine());
            } catch (Exception e) {
                throw TwigRuntimeException.badOperatorFailedNode(operatorToken.getValue(), parser.getFilename(), operatorToken.getLine(), e);
            }

            token = parser.getCurrentToken();
        }

        return expr;
    }

    /**
     * TODO find a good doscription of this method
     *
     * @return The node for this expression
     * @throws SyntaxErrorException On syntax errors
     */
    protected Node getPrimary() throws SyntaxErrorException, TwigRuntimeException {
        // TODO check if unary or is opening parenthesis
        return parsePrimaryExpression();
    }

    /**
     * TODO find a good description of this method
     *
     * @return A node for the expression
     * @throws SyntaxErrorException On syntax errors
     */
    public Node parsePrimaryExpression() throws SyntaxErrorException, TwigRuntimeException {
        Token token = parser.getCurrentToken();
        Node node;

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
    public Node parseStringExpression() throws SyntaxErrorException, TwigRuntimeException {
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
        Node expression = nodes.remove(0);
        for (Node node : nodes) {
            expression = new BinaryConcat(expression, node, node.getLine());
        }

        return expression;
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
