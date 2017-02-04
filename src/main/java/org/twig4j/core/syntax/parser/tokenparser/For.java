package org.twig4j.core.syntax.parser.tokenparser;

import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.TokenStream;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.type.expression.Expression;

import java.util.List;

public class For extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException {
        Integer line = token.getLine();
        TokenStream tokenStream = parser.getTokenStream();
        List<String> targets = parser.getExpressionParser().parseAssignmentExpression();
        tokenStream.expect(Token.Type.OPERATOR, "in");
        Expression seq = parser.getExpressionParser().parseExpression();
        org.twig4j.core.syntax.parser.node.type.For.Settings settings = new org.twig4j.core.syntax.parser.node.type.For.Settings();

        if (tokenStream.getCurrent().test(Token.Type.NAME, "if")) {
            tokenStream.next();
            settings.setIfExpr(parser.getExpressionParser().parseExpression());
        }

        tokenStream.expect(Token.Type.BLOCK_END);

        settings.setBody(this.parser.subparse(this::decideForFork, null, false));
        settings.setSeq(seq);

        if (tokenStream.getCurrent().is(Token.Type.NAME, "else")) {
            tokenStream.next();
            tokenStream.expect(Token.Type.BLOCK_END);
            settings.setElseBody(parser.subparse(this::decideForEnd, null, true));
        } else {
            tokenStream.next();
        }

        tokenStream.expect(Token.Type.BLOCK_END);

        if (targets.size() > 1) {
            settings.setKeyTarget(targets.get(0));
            settings.setValueTarget(targets.get(1));
        } else {
            settings.setValueTarget(targets.get(0));
        }

        // TODO check loop variables

        return new org.twig4j.core.syntax.parser.node.type.For(settings, line, "for");
    }

    @Override
    public String getTag() {
        return "for";
    }

    /**
     * Return whether the token is an else or a for tag
     *
     * @param token The token to test
     *
     * @return Whether this is a fork (or end) tag
     */
    public boolean decideForFork(Token token) {
        return token.is(Token.Type.NAME, "else") || token.is(Token.Type.NAME, "endfor");
    }

    /**
     * Return whether the token is an endfor tag
     *
     * @param token The token to test
     *
     * @return Whether this is the end of a for tag
     */
    public boolean decideForEnd(Token token) {
        return token.is(Token.Type.NAME, "endfor");
    }
}
