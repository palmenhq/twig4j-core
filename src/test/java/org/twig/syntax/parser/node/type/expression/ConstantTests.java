package org.twig.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twig.compiler.ClassCompiler;

import static org.mockito.Mockito.*;

public class ConstantTests {
    @Test
    public void canWriteRepresent() {
        ClassCompiler compiler = mock(ClassCompiler.class);
        Constant constant = new Constant(1);
        constant.putAttribute("data", "foo");

        constant.compile(compiler);

        verify(compiler).representValue("foo");
    }
}
