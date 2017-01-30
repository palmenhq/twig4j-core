package org.twig.syntax.parser.node;

import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.type.Body;

import static org.mockito.Mockito.*;

public class ModuleTests {
    @Test
    public void canCompileHeaderAndFooter() throws LoaderException, TwigRuntimeException {
        ClassCompiler classCompilerStub = mock(ClassCompiler.class);
        Environment environmentStub = mock(Environment.class);

        Module module = new Module(new Body(1));
        module.setFileName("foo");

        setupClassCompilerStubWhens(classCompilerStub, environmentStub);

        module.compile(classCompilerStub);

        verify(classCompilerStub).writeLine("package org.twig.template;\n");
        verify(classCompilerStub).writeLine("/**");
        verify(classCompilerStub).writeLine(" * foo");
        verify(classCompilerStub).writeLine(" */");
        verify(classCompilerStub).write("public class hash_0");
        verify(classCompilerStub).writeLine(" extends org.twig.template.Template {");
        verify(classCompilerStub, times(3)).indent();
        verify(classCompilerStub).writeLine("public String getTemplateName() {");
        verify(classCompilerStub).write("return ");
        verify(classCompilerStub).writeString("foo");
        verify(classCompilerStub).writeRaw(";\n");
        verify(classCompilerStub, times(3)).unIndent();
        verify(classCompilerStub, times(3)).writeLine("}");
    }

    @Test
    public void canCompileBody() throws LoaderException, TwigRuntimeException {
        ClassCompiler classCompilerStub = mock(ClassCompiler.class);
        Environment environmentStub = mock(Environment.class);

        Body bodyNodeStub = mock(Body.class);

        Module module = new Module(bodyNodeStub);
        module.setFileName("foo");

        setupClassCompilerStubWhens(classCompilerStub, environmentStub);

        module.compile(classCompilerStub);

        verify(classCompilerStub).subCompile(bodyNodeStub);
        verify(classCompilerStub).writeLine("protected String doRender(Context context) throws TwigException {");
        verify(classCompilerStub, times(3)).writeLine("}");
    }

    private void setupClassCompilerStubWhens(ClassCompiler classCompilerStub, Environment environmentStub) throws LoaderException, TwigRuntimeException {
        when(classCompilerStub.writeLine(anyString())).thenReturn(classCompilerStub);
        when(classCompilerStub.write(anyString())).thenReturn(classCompilerStub);
        when(classCompilerStub.writeRaw(anyString())).thenReturn(classCompilerStub);
        when(classCompilerStub.writeString(anyString())).thenReturn(classCompilerStub);
        when(classCompilerStub.indent()).thenReturn(classCompilerStub);
        when(classCompilerStub.unIndent()).thenReturn(classCompilerStub);
        when(classCompilerStub.subCompile(anyObject())).thenReturn(classCompilerStub);
        when(classCompilerStub.compile(anyObject())).thenReturn(classCompilerStub);
        when(classCompilerStub.getEnvironment()).thenReturn(environmentStub);

        when(environmentStub.getTemplateClass("foo")).thenReturn("hash_0");
        when(environmentStub.getTemplateBaseClass()).thenReturn("org.twig.template.Template");
        when(environmentStub.getTemplatePackage()).thenReturn("org.twig.template");
    }

}
