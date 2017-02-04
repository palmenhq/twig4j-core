package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twig4j.core.compiler.ClassCompiler;

import static org.mockito.Mockito.*;

public class ConstantTests {
    @Test
    public void canWriteRepresent() {
        ClassCompiler compiler = mock(ClassCompiler.class);
        Constant constant = new Constant(1);
        constant.putAttribute("data", "1");

        constant.compile(compiler);

        verify(compiler).representValue("1");
    }
}
