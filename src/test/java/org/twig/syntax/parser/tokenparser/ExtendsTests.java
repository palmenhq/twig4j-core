package org.twig.syntax.parser.tokenparser;

import org.junit.Test;
import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.TokenStream;
import org.twig.syntax.Token;
import org.twig.syntax.parser.ExpressionParser;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.type.expression.StringConstant;

import static org.mockito.Mockito.*;

public class ExtendsTests {
    @Test
    public void canParseExtends() throws SyntaxErrorException, TwigRuntimeException {
        Extends extendsParser = new Extends();

        Parser parser = mock(Parser.class);
        ExpressionParser expressionParser = mock(ExpressionParser.class);
        extendsParser.setParser(parser);

        when(parser.isMainScope()).thenReturn(true);
        when(parser.getParent()).thenReturn(null);
        when(parser.getExpressionParser()).thenReturn(expressionParser);

        StringConstant templateName = new StringConstant("foo.twig", 1);
        when(expressionParser.parseExpression()).thenReturn(templateName);
        when(parser.getTokenStream()).thenReturn(mock(TokenStream.class));

        extendsParser.parse(new Token(Token.Type.NAME, "extends", 1));

        verify(parser).setParent(templateName);
    }

    @Test(expected = SyntaxErrorException.class)
    public void cantExtendInsideBlock() throws SyntaxErrorException, TwigRuntimeException {
        Extends extendsParser = new Extends();

        Parser parser = mock(Parser.class);
        ExpressionParser expressionParser = mock(ExpressionParser.class);
        extendsParser.setParser(parser);

        when(parser.isMainScope()).thenReturn(false);
        extendsParser.parse(new Token(Token.Type.NAME, "extends", 1));
    }

    @Test(expected = SyntaxErrorException.class)
    public void cantUseMultipleExtends() throws SyntaxErrorException, TwigRuntimeException {
        Extends extendsParser = new Extends();

        Parser parser = mock(Parser.class);
        ExpressionParser expressionParser = mock(ExpressionParser.class);
        extendsParser.setParser(parser);

        when(parser.isMainScope()).thenReturn(true);
        when(parser.getParent()).thenReturn(new StringConstant("foo", 1));

        extendsParser.parse(new Token(Token.Type.NAME, "extends", 1));
    }
}
