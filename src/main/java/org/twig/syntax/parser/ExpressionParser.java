package org.twig.syntax.parser;

import org.twig.exception.SyntaxErrorException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.BinaryConcat;
import org.twig.syntax.parser.node.type.expression.Constant;

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

    public Node parseExpression() {
        return parseExpression(0);
    }

    public Node parseExpression(Integer precedence) {
        return new Node(1);
    }

    public Node parsePrimaryExpression() throws SyntaxErrorException {
        Token token = parser.getCurrentToken();
        Node node;

        switch(token.getType()) {
            case NAME:
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
                        // TODO create name
                        node = new Node(token.getLine());
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
     * @return The nodes that represents the string expression
     * @throws SyntaxErrorException
     */
    public Node parseStringExpression() throws SyntaxErrorException {
        Token currentToken = parser.getCurrentToken();
        TokenStream stream = parser.getTokenStream();
        ArrayList<Node> nodes = new ArrayList<>();

        // Must be a string to parse the current string
        if (stream.getCurrent().getType() != Token.Type.STRING) {
            throw SyntaxErrorException.unexpectedToken(stream.getCurrent(), parser.getFilename(), stream.getCurrent().getLine());
        }

        // Add the current string
        nodes.add(new Constant(currentToken.getValue(), currentToken.getLine()));
        // a string cannot be followed by another string in a single expression
        Boolean nextCanBeString = false;

        while (true) {
            if (nextCanBeString && stream.nextIs(Token.Type.STRING)) {
                Token token = stream.next();
                nodes.add(new Constant(token.getValue(), token.getLine()));
                nextCanBeString = false;
            } else if (stream.nextIs(Token.Type.INTERPLATION_START)) {
                throw new RuntimeException("String interpolation not implemented yet"); // TODO fix string interpolation
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
