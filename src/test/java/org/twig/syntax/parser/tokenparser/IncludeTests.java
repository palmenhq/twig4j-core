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
import org.twig.syntax.parser.node.type.expression.Constant;

public class IncludeTests {
    @Test
    public void canParseSimpleInclude() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream();
        // "include('foo.twig') %}"
        tokenStream.add(new Token(Token.Type.PUNCTUATION, "(", 1));
        tokenStream.add(new Token(Token.Type.STRING, "foo.twig", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ")", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        Include includeParser = new Include();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        includeParser.setParser(parser);

        Node includeNode = includeParser.parse(tokenStream.getCurrent());

        Assert.assertEquals("Should be correct node type", org.twig.syntax.parser.node.type.Include.class, includeNode.getClass());
        Assert.assertTrue("First node (filename expression) should be a constant", includeNode.getNode(0) instanceof Constant);
        Assert.assertEquals("Node should have correct filename", "foo.twig", includeNode.getNode(0).getAttribute("data"));
    }
}
