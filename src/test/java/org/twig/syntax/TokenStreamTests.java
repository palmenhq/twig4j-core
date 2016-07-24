package org.twig.syntax;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.SyntaxErrorException;

public class TokenStreamTests {
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
    public void canLook() throws SyntaxErrorException {
        TokenStream tokenStream = new TokenStream("aFile");
        Token token1 = new Token(Token.Type.TEXT, "foo", 1);
        tokenStream.add(token1);

        Assert.assertSame("Look at token at index 0 should return 1st token", token1, tokenStream.look(0));
        Assert.assertSame("1st nexted token should be 1st token even after looking", token1, tokenStream.next());
    }
}
 