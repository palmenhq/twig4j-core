package org.twig4j.core.syntax;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.twig4j.core.Environment;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.exception.Twig4jRuntimeException;

public class LexerTests {
    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    @Test
    public void lexesCodeWithDataOnly() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "data";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Assert.assertEquals("Data should be tokenized", code, tokenStream.next().getValue());
    }

    @Test
    public void addsEOF() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "data";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        tokenStream.next();
        Assert.assertEquals("Last token should be EOF", Token.Type.EOF, tokenStream.next().getType());
    }

    @Test
    public void lexesVariables() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
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
    public void lexesMultilineVariables() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "{{\n" +
                "aVariable\n" +
                "}}";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Token variableStart = tokenStream.next();
        Assert.assertEquals("Variable start token type should be of type VAR_START.", Token.Type.VAR_START, variableStart.getType());
        Token variableName = tokenStream.next();
        Assert.assertEquals("Variable name token type should be of type NAME.", Token.Type.NAME, variableName.getType());
        Assert.assertEquals("Variable name token value should be the variable name.", "aVariable", variableName.getValue());
        Token variableEnd = tokenStream.next();
        Assert.assertEquals("Variable end token type should be of type VAR_END.", Token.Type.VAR_END, variableEnd.getType());
    }

    @Test
    public void cantLexUnclosedVariable() throws SyntaxErrorException, Twig4jRuntimeException {
        expectedException.expect(SyntaxErrorException.class);
        expectedException.expectMessage("Unclosed variable in \"aFile\" at line 1.");

        Lexer lexer = new Lexer(new Environment());
        String code = "{{ unclosedVariable ";

        lexer.tokenize(code, "aFile");
    }

    @Test
    public void lexesDoulbeQuotedStrings() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code  = "{{ \"A string\" }}";

        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be variable start", Token.Type.VAR_START, tokenStream.next().getType());
        Token stringToken = tokenStream.next();
        Assert.assertEquals("2nd token should be of type STRING", Token.Type.STRING, stringToken.getType());
        Assert.assertEquals("STRING token value should be string contents", "A string", stringToken.getValue());
    }

    @Test
    public void lexesEscapedDoubleQuotedStrings() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code  = "{{ \"A \\\"string\" }}";

        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be variable start", Token.Type.VAR_START, tokenStream.next().getType());
        Token stringToken = tokenStream.next();
        Assert.assertEquals("2nd token should be of type STRING", Token.Type.STRING, stringToken.getType());
        Assert.assertEquals("STRING token value should be the escaped string contents", "A \"string", stringToken.getValue());
    }

    @Test
    public void lexesSingleQuotedStrings() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code  = "{{ 'A string' }}";

        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be variable start", Token.Type.VAR_START, tokenStream.next().getType());
        Token stringToken = tokenStream.next();
        Assert.assertEquals("2nd token should be of type STRING", Token.Type.STRING, stringToken.getType());
        Assert.assertEquals("STRING token value should be the string contents", "A string", stringToken.getValue());
    }

    @Test
    public void lexesEscapedSingleQuotedStrings() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code  = "{{ 'A \\'string' }}";

        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be variable start", Token.Type.VAR_START, tokenStream.next().getType());
        Token stringToken = tokenStream.next();
        Assert.assertEquals("2nd token should be of type STRING", Token.Type.STRING, stringToken.getType());
        Assert.assertEquals("STRING token value should be the escaped string contents", "A 'string", stringToken.getValue());
    }

    @Test
    public void lexesIntegers() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "{{ 1 }}";
        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be var start", Token.Type.VAR_START, tokenStream.next().getType());
        Token numberToken = tokenStream.next();
        Assert.assertEquals("2nd token should be of type number", Token.Type.NUMBER, numberToken.getType());
        Assert.assertEquals("2nd token should have value \"1\"", "1", numberToken.getValue());
    }

    @Test
    public void lexesIntegersWithAddOperator() throws SyntaxErrorException, Twig4jRuntimeException {
        Environment environment = new Environment();
        Lexer lexer = new Lexer(environment);
        String code = "{{ 1 + 1 }}";
        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be var start", Token.Type.VAR_START, tokenStream.next().getType());
        Token number1Token = tokenStream.next();
        Assert.assertEquals("2nd token should be of type number", Token.Type.NUMBER, number1Token.getType());
        Assert.assertEquals("2nd token should have value \"1\"", "1", number1Token.getValue());
        Token additionToken = tokenStream.next();
        Assert.assertEquals("3rd token should be operator", Token.Type.OPERATOR, additionToken.getType());
        Assert.assertEquals("3rd token should be a plus", "+", additionToken.getValue());
        Token number2Token = tokenStream.next();
        Assert.assertEquals("4th token should be of type number", Token.Type.NUMBER, number2Token.getType());
        Assert.assertEquals("4th ~token should have value \"1\"", "1", number2Token.getValue());
    }

    @Test
    public void cantLexUnclosedString() throws SyntaxErrorException, Twig4jRuntimeException {
        expectedException.expect(SyntaxErrorException.class);
        expectedException.expectMessage("Unclosed \" in \"file\" at line 1.");

        Lexer lexer = new Lexer(new Environment());
        String code  = "{{ \"A un unclosed string }}";

        TokenStream tokenStream = lexer.tokenize(code, "file");
    }

    @Test
    public void lexesInterploatedStrings() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code  = "foo {{ \"A #{variable} string\" }} bar";

        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be regular text", Token.Type.TEXT, tokenStream.next().getType());
        Assert.assertEquals("2nd token should be var start", Token.Type.VAR_START, tokenStream.next().getType());

        Token stringToken = tokenStream.next();
        Assert.assertEquals("3nd token should be of type STRING", Token.Type.STRING, stringToken.getType());
        Assert.assertEquals("3rd token value should be string contents before interolation start", "A ", stringToken.getValue());

        Assert.assertEquals("4th token should be interpolation start", Token.Type.INTERPLATION_START, tokenStream.next().getType());
        Token variableToken = tokenStream.next();
        Assert.assertEquals("5th token should be of type NAME ", Token.Type.NAME, variableToken.getType());
        Assert.assertEquals("NAME token should be have correct name", "variable", variableToken.getValue());
        Assert.assertEquals("6th token should be interpolation end", Token.Type.INTERPOLATION_END, tokenStream.next().getType());

        Token secondStringToken = tokenStream.next();
        Assert.assertEquals("7th token should be another string", Token.Type.STRING, secondStringToken.getType());
        Assert.assertEquals("String token should have correct string value", " string", secondStringToken.getValue());

        Assert.assertEquals("8th token should be a var end type", Token.Type.VAR_END, tokenStream.next().getType());
        Assert.assertEquals("9th token should be text", Token.Type.TEXT, tokenStream.next().getType());
    }

    @Test
    public void lexesPunctuation() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "{{ [foo.bar()] }}";

        TokenStream tokenStream = lexer.tokenize(code, "file");

        Assert.assertEquals("1st token should be var start", Token.Type.VAR_START, tokenStream.next().getType());

        Token squareBracket = tokenStream.next();
        Assert.assertEquals("2nd token should be a punctuation", Token.Type.PUNCTUATION, squareBracket.getType());
        Assert.assertEquals("2nd token should be a square bracket", "[", squareBracket.getValue());

        Assert.assertEquals("3rd token should be a name", Token.Type.NAME, tokenStream.next().getType());

        Token dot = tokenStream.next();
        Assert.assertEquals("4th token should be a punctuation", Token.Type.PUNCTUATION, dot.getType());
        Assert.assertEquals("4th token should be a dot", ".", dot.getValue());

        Assert.assertEquals("5th token should be a name", Token.Type.NAME, tokenStream.next().getType());

        Token openingParenthesis = tokenStream.next();
        Assert.assertEquals("6th token should be a punctuation", Token.Type.PUNCTUATION, openingParenthesis.getType());
        Assert.assertEquals("6th token should be an opening parenthesis", "(", openingParenthesis.getValue());

        Token closingParenthesis = tokenStream.next();
        Assert.assertEquals("7th token should be a punctuation", Token.Type.PUNCTUATION, closingParenthesis.getType());
        Assert.assertEquals("7th token should be a closing parenthesis", ")", closingParenthesis.getValue());

        Token closingSquareBracket = tokenStream.next();
        Assert.assertEquals("8th token should be a punctuation", Token.Type.PUNCTUATION, closingSquareBracket.getType());
        Assert.assertEquals("8th token should be a closing square bracket", "]", closingSquareBracket.getValue());
    }

    @Test(expected = SyntaxErrorException.class)
    public void throwsErrorOnUnclosedParenthesis() throws Twig4jException {
        Lexer lexer = new Lexer(new Environment());
        String code = "{{ foo( }}";

        lexer.tokenize(code, "foo");
    }

    @Test
    public void canLexBlocks() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "{% aBlock %}";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Assert.assertEquals("1st token should be a block start", Token.Type.BLOCK_START, tokenStream.next().getType());

        Token blockName = tokenStream.next();
        Assert.assertEquals("2nd token should be a name", Token.Type.NAME, blockName.getType());
        Assert.assertEquals("3rd token should be the block name", "aBlock", blockName.getValue());

        Assert.assertEquals("4th token should be block end", Token.Type.BLOCK_END, tokenStream.next().getType());
    }

    @Test
    public void canLexMulitilneBlocks() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "{%\n" +
                "aBlock\n" +
                "%}";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Assert.assertEquals("1st token should be a block start", Token.Type.BLOCK_START, tokenStream.next().getType());

        Token blockName = tokenStream.next();
        Assert.assertEquals("2nd token should be a name", Token.Type.NAME, blockName.getType());
        Assert.assertEquals("3rd token should be the block name", "aBlock", blockName.getValue());

        Assert.assertEquals("4th token should be block end", Token.Type.BLOCK_END, tokenStream.next().getType());
    }

    @Test
    public void ignoresComments() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "foo {# This is a comment #} bar";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");

        Assert.assertEquals("1st token should be text", "foo ", tokenStream.next().getValue());
        Assert.assertEquals("2nd token should be text", " bar", tokenStream.next().getValue());
    }

    @Test
    public void canHandleMultilineComments() throws SyntaxErrorException, Twig4jRuntimeException {
        Lexer lexer = new Lexer(new Environment());
        String code = "foo {# This \n" +
                "is a \n" +
                "multiline comment" +
                "\n" +
                "\n" +
                "#}bar";

        TokenStream tokenStream = lexer.tokenize(code, "aFile");
        Assert.assertEquals("1st token should be text", "foo ", tokenStream.next().getValue());
        Assert.assertEquals("2nd token should be text", "bar", tokenStream.next().getValue());
    }
}
