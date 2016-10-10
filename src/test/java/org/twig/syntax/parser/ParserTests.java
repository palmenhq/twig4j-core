package org.twig.syntax.parser;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.SyntaxErrorException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.node.Module;
import org.twig.syntax.parser.node.Node;

import java.util.ArrayList;

public class ParserTests {
    @Test
    public void canParseText() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.TEXT, "Hello world!", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 2));

        Parser parser = new Parser();
        Module module = parser.parse(tokenStream);

        Assert.assertEquals("Module should have one node", 1, module.getBodyNodes().size());

        ArrayList<Node> bodyNodes = module.getBodyNodes();
        Assert.assertEquals("One (text) node should be parsed", 1, bodyNodes.size());
        Assert.assertEquals("Module node 1 should be of type text", "Hello world!", bodyNodes.get(0).getAttribute("data"));
    }
}
