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
import org.twig4j.core.syntax.parser.node.type.expression.BinaryRange;

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
