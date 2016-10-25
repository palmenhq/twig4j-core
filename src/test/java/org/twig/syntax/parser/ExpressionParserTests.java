package org.twig.syntax.parser;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.SyntaxErrorException;
import org.twig.syntax.Token;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.BinaryConcat;
import org.twig.syntax.parser.node.type.expression.Constant;
import org.twig.syntax.parser.node.type.expression.Name;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ExpressionParserTests {
    @Test
    public void testParsePrimaryExpressionBool() throws SyntaxErrorException {
        Token trueToken = new Token(Token.Type.NAME, "true", 1);
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(trueToken);

        Parser parserStub = mock(Parser.class);
        when(parserStub.getCurrentToken()).thenReturn(trueToken);

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
    public void testParsePrimaryExpressionNull() throws SyntaxErrorException {
        Token nullToken = new Token(Token.Type.NAME, "null", 1);
        TokenStream tokenStream = new TokenStream();
        tokenStream.add(nullToken);

        Parser parserStub = mock(Parser.class);
        when(parserStub.getCurrentToken()).thenReturn(nullToken);

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
    public void testParseSimpleString() throws SyntaxErrorException {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.STRING, "foo", 1));
        TokenStream tokenStream = new TokenStream(tokens);
        Parser parser = mock(Parser.class);

        ExpressionParser expressionParser = new ExpressionParser(parser);

        when(parser.getTokenStream()).thenReturn(tokenStream);
        when(parser.getCurrentToken()).thenReturn(tokens.get(0));

        Node parsedString = expressionParser.parseStringExpression();

        Node expectedNode = new Constant("foo", 1);

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

//    @Test
//    public void testParseInterpolatedString() throws SyntaxErrorException {
//        ArrayList<Token> tokens = new ArrayList<>();
//        // Token stream for "foo#{"bar"}"
//        //tokens.add(new Token(Token.Type.VAR_START, null, 1));
//        tokens.add(new Token(Token.Type.STRING, "foo", 1));
//        tokens.add(new Token(Token.Type.INTERPLATION_START, null, 1));
//        tokens.add(new Token(Token.Type.STRING, "bar", 1));
//        tokens.add(new Token(Token.Type.INTERPOLATION_END, null, 1));
//        TokenStream tokenStream = new TokenStream(tokens);
//
//        Parser parser = new Parser();
//        ExpressionParser expressionParser = new ExpressionParser(parser);
//        parser
//                .setExpressionParser(expressionParser)
//                .setTokenStream(tokenStream);
//
//        Node parsedString = expressionParser.parseStringExpression();
//
//        BinaryConcat expectedNode = new BinaryConcat(new Constant("foo", 1), new Constant("bar", 1), 1);
//
//        Assert.assertEquals(
//                "Type of returned node should be BinaryConcat",
//                expectedNode.getClass(),
//                parsedString.getClass()
//        );
//        Assert.assertEquals(
//                "Type of left should be Constant",
//                expectedNode.getLeftNode().getClass(),
//                parsedString.getNode(0).getClass()
//        );
//        Assert.assertEquals(
//                "Value of left should be \"foo\"",
//                expectedNode.getLeftNode().getAttribute("data"),
//                parsedString.getNode(0).getAttribute("data")
//        );
//        Assert.assertEquals(
//                "Type of right should be Constant",
//                expectedNode.getRightNode().getClass(),
//                parsedString.getNode(1).getClass()
//        );
//        Assert.assertEquals(
//                "Value of right should be \"bar\"",
//                expectedNode.getRightNode().getAttribute("data"),
//                parsedString.getNode(1).getAttribute("data")
//        );
//    }

    @Test
    public void testParsePrimaryExpressionString() throws SyntaxErrorException {
        ArrayList<Token> tokens = new ArrayList<>();
        tokens.add(new Token(Token.Type.STRING, "foo", 1));
        TokenStream tokenStream = new TokenStream(tokens);
        Parser parser = mock(Parser.class);

        ExpressionParser expressionParser = new ExpressionParser(parser);

        when(parser.getTokenStream()).thenReturn(tokenStream);
        when(parser.getCurrentToken()).thenReturn(tokens.get(0));

        Node parsedString = expressionParser.parsePrimaryExpression();
        Node expectedNode = new Constant("foo", 1);

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
}
