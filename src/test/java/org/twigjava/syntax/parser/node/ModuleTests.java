package org.twigjava.syntax.parser.node;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.type.Body;

import java.util.HashMap;

import static org.mockito.Mockito.*;

public class ModuleTests {
    @Test
    public void canCompileHeaderAndFooter() throws LoaderException, TwigRuntimeException {
        Environment environmentStub = mock(Environment.class);
        ClassCompiler classCompiler = new ClassCompiler(environmentStub);

        Module module = new Module(new Body(1), null, new HashMap<>(), "foo");

        when(environmentStub.getTemplateClass(eq("foo"))).thenReturn("abc123");
        when(environmentStub.getTemplateBaseClass()).thenReturn("org.twigjava.template.Template");

        module.compile(classCompiler);

        Assert.assertTrue(
            "Module should compile without exploding",
            classCompiler.getSourceCode().length() > 0
        );
    }

}
