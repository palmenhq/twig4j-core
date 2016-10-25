package org.twig.syntax.parser.node.type;

import org.junit.Test;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.syntax.parser.node.type.expression.Constant;

import static org.mockito.Mockito.*;

public class PrintExpressionTests {
    @Test
    public void canCompile() throws LoaderException {
        ClassCompiler compiler = mock(ClassCompiler.class);
        Constant constant = new Constant(1);
        constant.addNode(constant);
        PrintExpression print = new PrintExpression(constant, 1);

        when(compiler.write(anyString())).thenReturn(compiler);
        when(compiler.writeRaw(anyString())).thenReturn(compiler);
        when(compiler.compile(anyObject())).thenReturn(compiler);

        print.compile(compiler);

        verify(compiler).write("output = output.concat(");
        verify(compiler).compile(constant);
        verify(compiler).writeRaw(");\n");
    }
}
