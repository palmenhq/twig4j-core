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
import org.twig4j.core.syntax.parser.node.type.BlockReference;
import org.twig4j.core.syntax.parser.node.type.Text;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

import static org.mockito.Mockito.*;

public class BlockTests {
    @Test
    public void canParseBlockReference() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream();
        // "a %}foo{% endblock %}"
        tokenStream.add(new Token(Token.Type.NAME, "a", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "endblock", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        Block blockParser = new Block();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        blockParser.setParser(parser);

        Node blockReference = blockParser.parse(tokenStream.getCurrent());

        Assert.assertEquals("Block reference should be instance of block reference", BlockReference.class, blockReference.getClass());
        Assert.assertEquals("Block reference name should be block name", "a", blockReference.getAttribute("name"));
        Assert.assertEquals(
            "Parser's block body should be of type text",
            Text.class,
            parser.getBlocks().get("a").getNode(0).getClass()
        );

    }

    @Test
    public void canParseBlockReferenceScopes() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream();
        // "a %}foo{% endblock %}"
        tokenStream.add(new Token(Token.Type.NAME, "a", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "endblock", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        Block blockParser = new Block();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        blockParser.setParser(parser);

        Stack<String> blockStack = mock(Stack.class);
        Map<String, org.twig4j.core.syntax.parser.node.type.Block> blocks = mock(HashMap.class);

        parser
            .setBlockStack(blockStack)
            .setBlocks(blocks);

        Node blockReference = blockParser.parse(tokenStream.getCurrent());

        verify(blockStack).push(eq("a"));
        verify(blockStack).pop();
        verify(blocks).put(eq("a"), anyObject());
    }
}
