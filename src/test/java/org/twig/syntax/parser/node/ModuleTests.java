package org.twig.syntax.parser.node;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.type.Body;

import java.util.HashMap;

import static org.mockito.Mockito.*;

public class ModuleTests {
    @Test
    public void canCompileHeaderAndFooter() throws LoaderException, TwigRuntimeException {
        Environment environmentStub = mock(Environment.class);
        ClassCompiler classCompiler = new ClassCompiler(environmentStub);

        Module module = new Module(new Body(1), null, new HashMap<>(), "foo");

        when(environmentStub.getTemplateClass(eq("foo"))).thenReturn("abc123");
        when(environmentStub.getTemplateBaseClass()).thenReturn("org.twig.template.Template");

        module.compile(classCompiler);

        Assert.assertTrue(
            "Module should compile without exploding",
            classCompiler.getSourceCode().length() > 0
        );
    }

}
