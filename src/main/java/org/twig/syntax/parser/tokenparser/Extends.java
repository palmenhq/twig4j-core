package org.twig.syntax.parser.tokenparser;

import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.parser.node.Node;

public class Extends extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException {
        if (!parser.isMainScope()) {
            throw new SyntaxErrorException("Cannot extend from a block", parser.getFilename(), token.getLine());
        }

        if (parser.getParent() != null) {
            throw new SyntaxErrorException("Multiple extends tags are forbidden", parser.getFilename(), token.getLine());
        }

        parser.setParent(parser.getExpressionParser().parseExpression());

        parser.getTokenStream().expect(Token.Type.BLOCK_END);

        return null;
    }

    @Override
    public String getTag() {
        return "extends";
    }
}
