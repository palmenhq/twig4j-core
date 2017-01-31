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
import org.twigjava.syntax.parser.node.type.expression.BinaryRange;

public class ForTests {
    @Test
    public void canParseFor() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream();
        // Parser has already parsed "{% for"
        tokenStream.add(new Token(Token.Type.NAME, "key", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ",", 1));
        tokenStream.add(new Token(Token.Type.NAME, "val", 1));
        tokenStream.add(new Token(Token.Type.OPERATOR, "in", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "1", 1));
        tokenStream.add(new Token(Token.Type.OPERATOR, "..", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "3", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "endfor", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        For forParser = new For();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        forParser.setParser(parser);

        Node forStatement = forParser.parse(tokenStream.getCurrent());

        Assert.assertEquals("Key target should be set", "key", forStatement.getAttribute("key_target"));
        Assert.assertEquals("Value target should be set", "val", forStatement.getAttribute("value_target"));
        Assert.assertTrue("Thing to loop over should be binary range", forStatement.getNode(0) instanceof BinaryRange);
        Assert.assertTrue("Body node should be a node", forStatement.getNode(1) instanceof Node);
    }
}
