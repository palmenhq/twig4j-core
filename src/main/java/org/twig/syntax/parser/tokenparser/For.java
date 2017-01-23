package org.twig.syntax.parser.tokenparser;

import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.Expression;

import java.util.List;

public class For extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException {
        Integer line = token.getLine();
        TokenStream tokenStream = parser.getTokenStream();
        List<String> targets = parser.getExpressionParser().parseAssignmentExpression();
        tokenStream.expect(Token.Type.OPERATOR, "in");
        Expression seq = parser.getExpressionParser().parseExpression();
        org.twig.syntax.parser.node.type.For.Settings settings = new org.twig.syntax.parser.node.type.For.Settings();

        // TODO check for ifexpr

        tokenStream.expect(Token.Type.BLOCK_END);

        settings.setBody(this.parser.subparse(this::decideForEnd, null, true));
        settings.setSeq(seq);
        // TODO else

        tokenStream.expect(Token.Type.BLOCK_END);

        if (targets.size() > 1) {
            settings.setKeyTarget(targets.get(0));
            settings.setValueTarget(targets.get(1));
        } else {
            settings.setValueTarget(targets.get(0));
        }

        // TODO the ifexpr body check

        // TODO check loop variables

        return new org.twig.syntax.parser.node.type.For(settings, line, "for");
    }

    @Override
    public String getTag() {
        return "for";
    }

    /**
     * Return whether the token is an endfor tag
     *
     * @param token
     * @return
     */
    public boolean decideForEnd(Token token) {
        // TODO test else
        return token.is(Token.Type.NAME, "endfor");
    }
}
