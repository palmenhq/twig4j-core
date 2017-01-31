package org.twigjava.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.exception.TwigException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.template.Template;

public class RuntimeTemplateCompilerTests {
    @Test
    public void testCompileJavaCode() throws TwigException {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler(new Environment());

        String sourceCode = "package org.twigjava.template;\n\n"
                + "public class TestTemplate extends org.twigjava.template.Template {\n"
                + "    protected String doDisplay(Context context, java.util.Map<String, TemplateBlockMethodSet> blocks) { return \"foo\"; }\n"
                + "    public String getTemplateName() { return \"foo\"; }\n"
                + "}\n";
        Template template = runtimeCompiler.compile(sourceCode, "org.twigjava.template.TestTemplate");

        Assert.assertEquals("Complied class method render() should return \"foo\"", "foo", template.render());
    }

    @Test(expected = TwigRuntimeException.class)
    public void testThrowsRuntimeErrorExceptionOnFailToCompile() throws TwigException {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler(new Environment());

        String sourceCode = "invalid java code";

        runtimeCompiler.compile(sourceCode, "something");
    }
}
