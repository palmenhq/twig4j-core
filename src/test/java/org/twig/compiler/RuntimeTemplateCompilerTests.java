package org.twig.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.template.Template;

public class RuntimeTemplateCompilerTests {
    @Test
    public void testCompileJavaCode() throws TwigException {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler(new Environment());

        String sourceCode = "package org.twig.template;\n\n"
                + "public class TestTemplate extends org.twig.template.Template {\n"
                + "    protected String doDisplay(Context context, java.util.Map<String, TemplateBlockMethodSet> blocks) { return \"foo\"; }\n"
                + "    public String getTemplateName() { return \"foo\"; }\n"
                + "}\n";
        Template template = runtimeCompiler.compile(sourceCode, "org.twig.template.TestTemplate");

        Assert.assertEquals("Complied class method render() should return \"foo\"", "foo", template.render());
    }

    @Test(expected = TwigRuntimeException.class)
    public void testThrowsRuntimeErrorExceptionOnFailToCompile() throws TwigException {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler(new Environment());

        String sourceCode = "invalid java code";

        runtimeCompiler.compile(sourceCode, "something");
    }
}
