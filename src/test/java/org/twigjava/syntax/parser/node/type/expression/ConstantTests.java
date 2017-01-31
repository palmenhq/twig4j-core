package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twigjava.compiler.ClassCompiler;

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
