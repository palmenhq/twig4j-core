package org.twig.syntax.parser.tokenparser;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.Node;

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

        Assert.assertEquals("Set statement should be a set statement", org.twig.syntax.parser.node.type.Set.class, setStatement.getClass());
    }
}
