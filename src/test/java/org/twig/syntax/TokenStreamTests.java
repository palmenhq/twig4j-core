package org.twig.syntax;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.twig.exception.SyntaxErrorException;

public class TokenStreamTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void canNext() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        Token token1 = new Token(Token.Type.TEXT, "foo", 1);
        tokenStream.add(token1);
        Token token2 = new Token(Token.Type.VAR_START, null, 1);
        tokenStream.add(token2);

        Assert.assertSame("1st nexted token should be token1", token1, tokenStream.next());
        Assert.assertSame("2nd nexted token should be token2", token2, tokenStream.next());
    }

    @Test
    public void throwsSyntaxErrorWhenTryingToAccessTokenOutOfBounds() throws SyntaxErrorException {
        expectedException.expect(SyntaxErrorException.class);
        expectedException.expectMessage("Unexpected end of template in \"file\" at line 1.");

        TokenStream tokenStream = new TokenStream("file");
        tokenStream.add(new Token(Token.Type.EOF, null, 1));
        tokenStream.next();
        tokenStream.next();
    }

    @Test
    public void canLookAtIndex() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        Token token1 = new Token(Token.Type.TEXT, "foo", 1);
        tokenStream.add(token1);

        Assert.assertSame("Look at token at index 0 should return 1st token", token1, tokenStream.look(0));
        Assert.assertSame("1st nexted token should be 1st token even after looking", token1, tokenStream.next());
    }

    @Test
    public void canLookAtNext() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        Token token1 = new Token(Token.Type.TEXT, "foo", 1);
        tokenStream.add(token1);

        Assert.assertSame("Look at token at next should return 1st token", token1, tokenStream.look());
        Assert.assertSame("1st nexted token should be 1st token even after looking", token1, tokenStream.next());
    }

    @Test
    public void canCheckIfEOF() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Assert.assertFalse("Should not be EOF when not at eof yet", tokenStream.isEOF());
        tokenStream.next();
        Assert.assertTrue("Should be eof when at eof", tokenStream.isEOF());
    }

    @Test
    public void canNextIs() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.STRING, "foo", 1));
        tokenStream.add(new Token(Token.Type.TEXT, "bar", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Assert.assertFalse("Next should not be type string", tokenStream.nextIs(Token.Type.STRING));
        Assert.assertTrue("Next should be type text", tokenStream.nextIs(Token.Type.TEXT));
        Assert.assertFalse("Next with wrong value should not be foo", tokenStream.nextIs(Token.Type.TEXT, "foo"));
        Assert.assertTrue("Next with value should be true", tokenStream.nextIs(Token.Type.TEXT, "bar"));
    }

    @Test(expected = SyntaxErrorException.class)
    public void expectWillThrowException() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.TEXT, "foo", 1));
        tokenStream.add(new Token(Token.Type.STRING, "bar", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        tokenStream.expect(Token.Type.BLOCK_START); // is actually TEXT
    }

    @Test
    public void canExpect() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        Token firstToken = new Token(Token.Type.TEXT, "foo", 1);
        Token expectedToken = new Token(Token.Type.STRING, "bar", 1);

        tokenStream.add(firstToken);
        tokenStream.add(expectedToken);
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Token foundToken = tokenStream.expect(Token.Type.TEXT);
        Assert.assertEquals("Found token from expect should be first token", firstToken, foundToken);
    }
}
