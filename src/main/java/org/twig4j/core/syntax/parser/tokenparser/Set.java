package org.twig4j.core.syntax.parser.tokenparser;

import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.TokenStream;
import org.twig4j.core.syntax.parser.node.Node;

import java.util.ArrayList;
import java.util.List;

public class Set extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException {
        Integer line = token.getLine();
        ArrayList<Node> tests = new ArrayList<>();
        TokenStream tokenStream = parser.getTokenStream();
        List<String> names = parser.getExpressionParser().parseAssignmentExpression();

        parser.getTokenStream().next();

        Node values = parser.getExpressionParser().parseMultitargetExpression();
        tokenStream.expect(Token.Type.BLOCK_END);

        if (names.size() != values.getNodes().size()) {
            throw new SyntaxErrorException("When using set, you must have the same number of variables and assignments.", parser.getFilename(), tokenStream.getCurrent().getLine());
        }

        return new org.twig4j.core.syntax.parser.node.type.Set(names, values, false, line, getTag());
    }

    @Override
    public String getTag() {
        return "set";
    }
}
