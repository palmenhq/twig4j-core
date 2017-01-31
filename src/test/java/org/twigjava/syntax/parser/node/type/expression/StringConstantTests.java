package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twigjava.compiler.ClassCompiler;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class StringConstantTests {
    @Test
    public void canWriteRepresent() {
        ClassCompiler compiler = mock(ClassCompiler.class);
        StringConstant constant = new StringConstant(1);
        constant.putAttribute("data", "foo");

        constant.compile(compiler);

        verify(compiler).writeString("foo");
    }
}
