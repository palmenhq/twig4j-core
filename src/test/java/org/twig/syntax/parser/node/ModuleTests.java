package org.twig.syntax.parser.node;

import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.loader.Loader;

import java.util.ArrayList;

import static org.mockito.Mockito.*;

public class ModuleTests {
    @Test
    public void canCompileEmptyClass() throws LoaderException {
        ClassCompiler classCompilerStub = mock(ClassCompiler.class);
        Environment environmentStub = mock(Environment.class);

        Module module = new Module(new ArrayList<>());
        module.setFileName("foo");

        when(classCompilerStub.writeLine(anyString())).thenReturn(classCompilerStub);
        when(classCompilerStub.write(anyString())).thenReturn(classCompilerStub);
        when(classCompilerStub.unIndent()).thenReturn(classCompilerStub);
        when(classCompilerStub.getEnvironment()).thenReturn(environmentStub);

        when(environmentStub.getTemplateClass("foo")).thenReturn("hash_0");
        when(environmentStub.getTemplateBaseClass()).thenReturn("BaseClass");

        module.compile(classCompilerStub);

        verify(classCompilerStub).writeLine("package org.twig.template;\n");
        verify(classCompilerStub).writeLine("/**");
        verify(classCompilerStub).writeLine(" * foo");
        verify(classCompilerStub).writeLine(" */");
        verify(classCompilerStub).write("public class hash_0");
        verify(classCompilerStub).writeLine(" extends BaseClass {");
        verify(classCompilerStub).indent();
    }
}
