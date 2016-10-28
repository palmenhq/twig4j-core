package org.twig.syntax.parser;

import org.twig.exception.SyntaxErrorException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
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
    private ArrayList<String> unaryOperators;
    private ArrayList<String> binaryOperators;

    public ExpressionParser(Parser parser) {
        this.parser = parser;
    }

    public ExpressionParser(Parser parser, ArrayList<String> unaryOperators, ArrayList<String> binaryOperators) {
        this.parser = parser;
        this.unaryOperators = unaryOperators;
        this.binaryOperators = binaryOperators;
    }

    /**
     * @see ExpressionParser#parseExpression(Integer) - defaults to 0
     */
    public Node parseExpression() throws SyntaxErrorException {
        return parseExpression(0);
    }

    /**
     * Parse an expression
     *
     * @param precedence TODO find out what this thing is
     * @return The node for the parsed expression
     */
    public Node parseExpression(Integer precedence) throws SyntaxErrorException {
        Node expr = getPrimary();

        // TODO do the binany while loop thing

        return expr;
    }

    /**
     * TODO find a good doscription of this method
     *
     * @return The node for this expression
     * @throws SyntaxErrorException On syntax errors
     */
    protected Node getPrimary() throws SyntaxErrorException {
        // TODO check if unary or is opening parenthesis
        return parsePrimaryExpression();
    }

    /**
     * TODO find a good description of this method
     *
     * @return A node for the expression
     * @throws SyntaxErrorException On syntax errors
     */
    public Node parsePrimaryExpression() throws SyntaxErrorException {
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
    public Node parseStringExpression() throws SyntaxErrorException {
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
}
