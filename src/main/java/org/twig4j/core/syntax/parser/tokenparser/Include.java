package org.twig4j.core.syntax.parser.tokenparser;

import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.type.expression.Expression;

public class Include extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, Twig4jRuntimeException {
        Expression templateName = parser.getExpressionParser().parseExpression();
        Boolean ignoreMissing = false;
        Boolean only = false;
        Expression variables = null;

        if (parser.getTokenStream().getCurrent().is(Token.Type.NAME, "ignore")) {
            parser.getTokenStream().next();
            parser.getTokenStream().expect(Token.Type.NAME, "missing");

            ignoreMissing = true;
        }

        if (parser.getTokenStream().getCurrent().is(Token.Type.NAME, "with")) {
            parser.getTokenStream().next();
            variables = parser.getExpressionParser().parseExpression();
        }

        if (parser.getTokenStream().getCurrent().is(Token.Type.NAME, "only")) {
            parser.getTokenStream().next();
            only = true;
        }

        parser.getTokenStream().expect(Token.Type.BLOCK_END);

        return new org.twig4j.core.syntax.parser.node.type.Include(
                templateName,
                variables,
                only,
                ignoreMissing,
                token.getLine(),
                getTag()
        );
    }

    @Override
    public String getTag() {
        return "include";
    }
}
