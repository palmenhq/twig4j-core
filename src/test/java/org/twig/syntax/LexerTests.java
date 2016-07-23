package org.twig.syntax;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.twig.exception.SyntaxErrorException;

public class LexerTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void lexesCodeWithDataOnly() throws SyntaxErrorException {
        Lexer lexer = new Lexer();
        String code = "data";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Assert.assertEquals(code, tokenStream.next().getValue());
    }

    @Test
    public void lexesVariables() throws SyntaxErrorException {
        Lexer lexer = new Lexer();
        String code = "data {{ aVariable }} more data";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Token firstData = tokenStream.next();
        Assert.assertEquals("Data (before variable) should be of type text.", Token.Type.TEXT, firstData.getType());
        Assert.assertEquals("Data (before variable) value should match.", "data ", firstData.getValue());

        Token variableStart = tokenStream.next();
        Assert.assertEquals("Variable start token type should be of type VAR_START.", Token.Type.VAR_START, variableStart.getType());
        Token variableName = tokenStream.next();
        Assert.assertEquals("Variable name token type should be of type NAME.", Token.Type.NAME, variableName.getType());
        Assert.assertEquals("Variable name token value should be the variable name.", "aVariable", variableName.getValue());
        Token variableEnd = tokenStream.next();
        Assert.assertEquals("Variable end token type should be of type VAR_END.", Token.Type.VAR_END, variableEnd.getType());

        Token lastData = tokenStream.next();
        Assert.assertEquals("Data (after variable) should be of type data.", Token.Type.TEXT, lastData.getType());
        Assert.assertEquals("Data (after variable) should match.", " more data", lastData.getValue());
    }

    @Test
    public void cantLexUnclosedVariable() throws SyntaxErrorException {
        expectedException.expect(SyntaxErrorException.class);
        expectedException.expectMessage("Unclosed variable in aFile at line 1.");

        Lexer lexer = new Lexer();
        String code = "{{ unclosedVariable ";

        lexer.tokenize(code, "aFile");
    }
}
