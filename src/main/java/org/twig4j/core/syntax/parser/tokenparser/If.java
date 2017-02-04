package org.twig4j.core.syntax.parser.tokenparser;

import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.TokenStream;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.type.control.ElseBody;
import org.twig4j.core.syntax.parser.node.type.control.ElseIfBody;
import org.twig4j.core.syntax.parser.node.type.control.IfBody;
import org.twig4j.core.syntax.parser.node.type.control.IfStatement;

import java.util.ArrayList;

public class If extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException {
        Integer line = token.getLine();
        ArrayList<Node> tests = new ArrayList<>();
        Node expression = parser.getExpressionParser().parseExpression();
        TokenStream tokenStream = parser.getTokenStream();

        tokenStream.expect(Token.Type.BLOCK_END);

        Node body = parser.subparse(this::decideIfFork, "if", false);

        tests.add(new IfBody(expression, body, line));

        boolean end = false;
        while (!end) {
            if (tokenStream.isEOF()) {
                String message = String.format(
                        "Unexpected end of template. Twig was looking for the following tags \"else\"," +
                                " \"elseif\", or \"endif\" to close the \"if\" block started at line %d)",
                        line
                );
                throw new SyntaxErrorException(message, parser.getFilename(), line);
            }

            switch (tokenStream.next().getValue()) {
                case "else":
                    Integer elseLine = tokenStream.getCurrent().getLine();
                    tokenStream.expect(Token.Type.BLOCK_END);
                    Node elseBody = parser.subparse(this::decideIfEnd, getTag(), false);
                    tests.add(new ElseBody(elseBody, elseLine));
                    break;

                case "elseif":
                    Integer elseIfLine = tokenStream.getCurrent().getLine();
                    Node elseIfConditional = parser.getExpressionParser().parseExpression();
                    tokenStream.expect(Token.Type.BLOCK_END);
                    Node elseIfbody = parser.subparse(this::decideIfFork, getTag(), false);

                    tests.add(new ElseIfBody(elseIfConditional, elseIfbody, elseIfLine));
                    break;

                case "endif":
                    end = true;
                    break;

                default:
                    String message = String.format(
                            "Unexpected end of template. Twig was looking for the following tags \"else\"," +
                                    " \"elseif\", or \"endif\" to close the \"if\" block started at line %d)",
                            line
                    );
                    throw new SyntaxErrorException(message, parser.getFilename(), line);
            }
        }

        tokenStream.expect(Token.Type.BLOCK_END);

        return new IfStatement(tests, line, getTag());
    }

    /**
     * Get whether the current token is another tag related to this if statemet (elseif, else, endif)
     * @param token The token to test
     * @return Whether the current token is related to this if tag
     */
    public boolean decideIfFork(Token token) {
        return token.is(Token.Type.NAME, "elseif") || token.is(Token.Type.NAME, "else") || token.is(Token.Type.NAME, "endif");
    }

    /**
     * Get whether the current token is an endif tag
     *
     * @param token The token to test
     * @return Whether the current token is endif
     */
    public boolean decideIfEnd(Token token) {
        return token.is(Token.Type.NAME, "endif");
    }
    @Override
    public String getTag() {
        return "if";
    }
}
