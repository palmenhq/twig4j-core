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
}
