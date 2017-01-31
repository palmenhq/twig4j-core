package org.twigjava.syntax.parser.tokenparser;

import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.Token;
import org.twigjava.syntax.parser.node.Node;

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
