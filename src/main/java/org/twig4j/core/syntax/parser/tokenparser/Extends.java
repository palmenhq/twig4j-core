package org.twig4j.core.syntax.parser.tokenparser;

import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.parser.node.Node;

public class Extends extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, Twig4jRuntimeException {
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
