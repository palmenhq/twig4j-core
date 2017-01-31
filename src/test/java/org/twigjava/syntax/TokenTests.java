package org.twigjava.syntax;

import org.junit.Assert;
import org.junit.Test;

public class TokenTests {
    @Test
    public void canIs() {
        Token token = new Token(Token.Type.STRING, "foo", 1);

        Assert.assertFalse("is() shouldn't be true if type is not the same", token.is(Token.Type.TEXT));
        Assert.assertTrue("is() should be true if type is the same", token.is(Token.Type.STRING));
        Assert.assertFalse("is() shouldn't be true if type and value aren't the same", token.is(Token.Type.STRING, "bar"));
        Assert.assertTrue("is() should be true if type and valueu are the same", token.is(Token.Type.STRING, "foo"));
    }
}
