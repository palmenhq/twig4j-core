package org.twig.syntax.parser;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.node.Module;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.PrintExpression;
import org.twig.syntax.parser.node.type.expression.*;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ExpressionParserTests {
    @Test
    public void testParsePrimaryExpressionBool() throws SyntaxErrorException, TwigRuntimeException {
        Token trueToken = new Token(Token.Type.NAME, "true", 1);
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(trueToken);
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parserStub = mock(Parser.class);
        when(parserStub.getCurrentToken()).thenReturn(trueToken);
        when(parserStub.getTokenStream()).thenReturn(tokenStream);

        ExpressionParser expressionParser = new ExpressionParser(parserStub);
        Node boolConstant = expressionParser.parsePrimaryExpression();

        Assert.assertEquals(
                "Returned node should be a Constant",
                Constant.class.toString(),
                boolConstant.getClass().toString()
        );
        Assert.assertEquals(
                "Returned node should have value true",
                "true",
                boolConstant.getAttribute("data")
        );
    }

    @Test
    public void testParsePrimaryExpressionNull() throws SyntaxErrorException, TwigRuntimeException {
        Token nullToken = new Token(Token.Type.NAME, "null", 1);
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(nullToken);
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parserStub = mock(Parser.class);
        when(parserStub.getCurrentToken()).thenReturn(nullToken);
        when(parserStub.getTokenStream()).thenReturn(tokenStream);

        ExpressionParser expressionParser = new ExpressionParser(parserStub);
        Node boolConstant = expressionParser.parsePrimaryExpression();

        Assert.assertEquals(
                "Returned node should be a Constant",
                Constant.class.toString(),
                boolConstant.getClass().toString()
        );
        Assert.assertEquals(
                "Returned node should have value null",
                "null",
                boolConstant.getAttribute("data")
        );
    }

    @Test
    public void testParseSimpleString() throws SyntaxErrorException, TwigRuntimeException {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.STRING, "foo", 1));
        tokens.add(new Token(Token.Type.EOF, null, 1));
        TokenStream tokenStream = new TokenStream(tokens);
        Parser parser = mock(Parser.class);

        ExpressionParser expressionParser = new ExpressionParser(parser);

        when(parser.getTokenStream()).thenReturn(tokenStream);
        when(parser.getCurrentToken()).thenReturn(tokens.get(0));

        Node parsedString = expressionParser.parseStringExpression();

        Node expectedNode = new StringConstant("foo", 1);

        Assert.assertEquals(
                "Type of returned node should be constant",
                expectedNode.getClass(),
                parsedString.getClass()
        );
        Assert.assertEquals(
                "Value should be \"foo\"",
                expectedNode.getAttribute("data"),
                parsedString.getAttribute("data")
        );
    }

    @Test
    public void testParseInterpolatedString() throws SyntaxErrorException, TwigRuntimeException {
        ArrayList<Token> tokens = new ArrayList<>();
        // Token stream for "foo#{"bar"}"
        //tokens.add(new Token(Token.Type.VAR_START, null, 1));
        tokens.add(new Token(Token.Type.STRING, "foo", 1));
        tokens.add(new Token(Token.Type.INTERPLATION_START, null, 1));
        tokens.add(new Token(Token.Type.STRING, "bar", 1));
        tokens.add(new Token(Token.Type.INTERPOLATION_END, null, 1));
        tokens.add(new Token(Token.Type.EOF, null, 1));
        TokenStream tokenStream = new TokenStream(tokens);

        Parser parser = new Parser(new Environment());
        ExpressionParser expressionParser = new ExpressionParser(parser);
        parser
                .setExpressionParser(expressionParser)
                .setTokenStream(tokenStream);

        Node parsedString = expressionParser.parseStringExpression();

        BinaryConcat expectedNode = new BinaryConcat(new StringConstant("foo", 1), new StringConstant("bar", 1), 1);

        Assert.assertEquals(
                "Type of returned node should be BinaryConcat",
                expectedNode.getClass(),
                parsedString.getClass()
        );
        Assert.assertEquals(
                "Type of left should be Constant",
                expectedNode.getLeftNode().getClass(),
                parsedString.getNode(0).getClass()
        );
        Assert.assertEquals(
                "Value of left should be \"foo\"",
                expectedNode.getLeftNode().getAttribute("data"),
                parsedString.getNode(0).getAttribute("data")
        );
        Assert.assertEquals(
                "Type of right should be Constant",
                expectedNode.getRightNode().getClass(),
                parsedString.getNode(1).getClass()
        );
        Assert.assertEquals(
                "Value of right should be \"bar\"",
                expectedNode.getRightNode().getAttribute("data"),
                parsedString.getNode(1).getAttribute("data")
        );
    }

    @Test
    public void testParsePrimaryExpressionString() throws SyntaxErrorException, TwigRuntimeException {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.STRING, "foo", 1));
        tokens.add(new Token(Token.Type.EOF, null, 1));
        TokenStream tokenStream = new TokenStream(tokens);
        Parser parser = mock(Parser.class);

        ExpressionParser expressionParser = new ExpressionParser(parser);

        when(parser.getTokenStream()).thenReturn(tokenStream);
        when(parser.getCurrentToken()).thenReturn(tokens.get(0));

        Node parsedString = expressionParser.parsePrimaryExpression();
        Node expectedNode = new StringConstant("foo", 1);

        Assert.assertEquals(
                "Type of returned node should be Constant",
                expectedNode.getClass(),
                parsedString.getClass()
        );
        Assert.assertEquals(
                "Value should be \"foo\"",
                expectedNode.getAttribute("data"),
                parsedString.getAttribute("data")
        );
    }

    @Test
    public void testParsePrimaryExpressionVariable() throws SyntaxErrorException, TwigRuntimeException {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.NAME, "foo", 1));
        tokens.add(new Token(Token.Type.EOF, null, 1));
        TokenStream tokenStream = new TokenStream(tokens);
        Parser parser = mock(Parser.class);

        ExpressionParser expressionParser = new ExpressionParser(parser);

        when(parser.getTokenStream()).thenReturn(tokenStream);
        when(parser.getCurrentToken()).thenReturn(tokens.get(0));

        Node parsedString = expressionParser.parsePrimaryExpression();
        Node expectedNode = new Name("foo", 1);

        Assert.assertEquals(
                "Type of returned node should be Name",
                expectedNode.getClass(),
                parsedString.getClass()
        );
        Assert.assertEquals(
                "Value should be \"foo\"",
                expectedNode.getAttribute("name"),
                parsedString.getAttribute("name")
        );
    }

    @Test
    public void canParseAddition() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "1", 1));
        tokenStream.add(new Token(Token.Type.OPERATOR, "+", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "2", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "Body node should be a PrintExpression",
                PrintExpression.class,
                module.getBodyNode().getClass()
        );
        Assert.assertEquals(
                "Printed expression should be a binary add",
                BinaryAdd.class,
                module.getBodyNode().getNode(0).getClass()
        );
        Assert.assertEquals(
                "Left item sholud be number 1",
                "1",
                module.getBodyNode().getNode(0).getNode(0).getAttribute("data")
        );
        Assert.assertEquals(
                "Right item sholud be number 2",
                "2",
                module.getBodyNode().getNode(0).getNode(1).getAttribute("data")
        );
    }

    @Test
    public void canParseMathsWithParenthesis() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, "(", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "1", 1));
        tokenStream.add(new Token(Token.Type.OPERATOR, "+", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "2", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ")", 1));
        tokenStream.add(new Token(Token.Type.OPERATOR, "*", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "2", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());
        Module module = parser.parse(tokenStream);

        Assert.assertEquals(
                "First node should be multiplication (since 1 + 1 is wrapped in parenthesises)",
                BinaryMultiply.class,
                module.getBodyNode().getNode(0).getClass()
        );
        Assert.assertEquals(
                "Multiplication node should have left expression binary addition",
                BinaryAdd.class,
                module.getBodyNode().getNode(0).getNode(0).getClass()
        );
        Assert.assertEquals(
                "Multiplication node should have right expression constant",
                Constant.class,
                module.getBodyNode().getNode(0).getNode(1).getClass()
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void cantParseUnclosedParenthesis() throws SyntaxErrorException, TwigRuntimeException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.VAR_START, null, 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, "(", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "1", 1));
        tokenStream.add(new Token(Token.Type.VAR_END, null, 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));

        Parser parser = new Parser(new Environment());
        parser.parse(tokenStream);
    }

    @Test
    public void canParseArguments() throws TwigException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.PUNCTUATION, "(", 1));
        tokenStream.add(new Token(Token.Type.NAME, "foo", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ",", 1));
        tokenStream.add(new Token(Token.Type.NUMBER, "1", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ")", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));
        Parser parserStub = new Parser(new Environment());
        parserStub.setTokenStream(tokenStream);

        ExpressionParser expressionParser = new ExpressionParser(parserStub);
        Node arguments = expressionParser.parseArguments();

        Assert.assertEquals(
                "First argument should be of type name",
                Name.class,
                arguments.getNode(0).getClass()
        );
        Assert.assertEquals(
                "First argument should have value \"foo\"",
                "foo",
                arguments.getNode(0).getAttribute("name")
        );
        Assert.assertEquals(
                "2nd argument should be of type constant",
                Constant.class,
                arguments.getNode(1).getClass()
        );
        Assert.assertEquals(
                "2nd argument should have value 1",
                "1",
                arguments.getNode(1).getAttribute("data")
        );
    }

    @Test
    public void canParseMethod() throws TwigException {
        TokenStream tokenStream = new TokenStream("aFile");
        tokenStream.add(new Token(Token.Type.NAME, "foo", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ".", 1));
        tokenStream.add(new Token(Token.Type.NAME, "bar", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, "(", 1));
        tokenStream.add(new Token(Token.Type.STRING, "baz", 1));
        tokenStream.add(new Token(Token.Type.PUNCTUATION, ")", 1));
        tokenStream.add(new Token(Token.Type.EOF, null, 1));
        Parser parserStub = new Parser(new Environment());
        parserStub.setTokenStream(tokenStream);

        ExpressionParser expressionParser = new ExpressionParser(parserStub);

        Node parsedExpression = expressionParser.parseExpression();

        Assert.assertEquals(
                "Returned node should be of type GetAttr",
                GetAttr.class,
                parsedExpression.getClass()
        );
        Assert.assertEquals(
                "1st getattr node (the node) should be of type name",
                Name.class,
                parsedExpression.getNode(0).getClass()
        );
        Assert.assertEquals(
                "1st getattr node (the node should be \"foo\"",
                "foo",
                parsedExpression.getNode(0).getAttribute("name")
        );
        Assert.assertEquals(
                "2nd getattr node (the attribute) should be of type constant",
                Constant.class,
                parsedExpression.getNode(1).getClass()
        );
        Assert.assertEquals(
                "2nd getattr node (the attribute) should be \"bar\"",
                "bar",
                parsedExpression.getNode(1).getAttribute("data")
        );
        Assert.assertEquals(
                "3rd getattr node (the arguments) should be of type array",
                Array.class,
                parsedExpression.getNode(2).getClass()
        );
        Assert.assertEquals(
                "3rd getattr node (the attribute) should contain 1 sub node (=1 argument)",
                1,
                parsedExpression.getNode(2).getNodes().size()
        );
    }
}
