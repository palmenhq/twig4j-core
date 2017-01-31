package org.twigjava.syntax.parser.tokenparser;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.Token;
import org.twigjava.syntax.TokenStream;
import org.twigjava.syntax.parser.Parser;
import org.twigjava.syntax.parser.node.Node;

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

        Assert.assertEquals("Set statement should be a set statement", org.twigjava.syntax.parser.node.type.Set.class, setStatement.getClass());
    }
}
