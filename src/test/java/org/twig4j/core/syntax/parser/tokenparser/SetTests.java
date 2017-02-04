package org.twig4j.core.syntax.parser.tokenparser;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.TokenStream;
import org.twig4j.core.syntax.parser.Parser;
import org.twig4j.core.syntax.parser.node.Node;

public class SetTests {
    @Test
    public void canParseSimpleSet() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(new Token(Token.Type.NAME, "foo", 1));
        tokenStream.add(new Token(Token.Type.OPERATOR, "=", 1));
        tokenStream.add(new Token(Token.Type.STRING, "bar", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        Set setParser = new Set();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        setParser.setParser(parser);

        Node setStatement = setParser.parse(tokenStream.getCurrent());

        Assert.assertEquals("Set statement should be a set statement", org.twig4j.core.syntax.parser.node.type.Set.class, setStatement.getClass());
    }
}
