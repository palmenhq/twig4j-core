package org.twig4j.core.syntax.parser.tokenparser;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.Token;
import org.twig4j.core.syntax.TokenStream;
import org.twig4j.core.syntax.parser.Parser;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.type.control.*;

public class IfTests {
    @Test
    public void canParseIf() throws SyntaxErrorException, Twig4jRuntimeException {
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(new Token(Token.Type.NAME, "true", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "endif", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        If ifParser = new If();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        ifParser.setParser(parser);

        Node ifStatement = ifParser.parse(tokenStream.getCurrent());

        Assert.assertEquals("If statement should be instance of if statement", IfStatement.class, ifStatement.getClass());
        Assert.assertEquals("First if statement node should be an if statement body", IfBody.class, ifStatement.getNode(0).getClass());
    }
    @Test
    public void canParseElseAndElse() throws SyntaxErrorException, Twig4jRuntimeException {
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(new Token(Token.Type.NAME, "true", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "elseif", 1));
        tokenStream.add(new Token(Token.Type.NAME, "true", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "bar", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "else", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));
        tokenStream.add(new Token(Token.Type.TEXT, "bar", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_START, null, 1));
        tokenStream.add(new Token(Token.Type.NAME, "endif", 1));
        tokenStream.add(new Token(Token.Type.BLOCK_END, null, 1));

        If ifParser = new If();
        Parser parser = new Parser(new Environment());
        parser.setTokenStream(tokenStream);
        ifParser.setParser(parser);

        Node ifStatement = ifParser.parse(tokenStream.getCurrent());

        Assert.assertEquals("If statement should be instance of if statement", IfStatement.class, ifStatement.getClass());
        Assert.assertEquals("First if statement node should be an if statement body", IfBody.class, ifStatement.getNode(0).getClass());
        Assert.assertEquals("First if statement node should be an elseif statement body", ElseIfBody.class, ifStatement.getNode(1).getClass());
        Assert.assertEquals("First if statement node should be an else statement body", ElseBody.class, ifStatement.getNode(2).getClass());
    }
}
