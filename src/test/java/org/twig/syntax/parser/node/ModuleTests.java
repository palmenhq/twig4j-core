package org.twig.syntax.parser.node;

import org.junit.Test;
import org.twig.compiler.ClassCompiler;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ModuleTests {
    @Test
    public void canCompileEmptyClass() {
        ClassCompiler classCompilerStub = mock(ClassCompiler.class);

        Module module = new Module(new ArrayList<>());
        module.setFileName("foo");

        module.compile(classCompilerStub);

        verify(classCompilerStub).writeLine("package org.twig.template;\n");
        verify(classCompilerStub).writeLine("/**");
        verify(classCompilerStub).writeLine(" * foo");
        verify(classCompilerStub).writeLine(" */");
        verify(classCompilerStub).write("public class TODO"); // TODO
        verify(classCompilerStub).writeLine(" extends BASECLASS {"); // TODO
        verify(classCompilerStub).indent();
    }
}
