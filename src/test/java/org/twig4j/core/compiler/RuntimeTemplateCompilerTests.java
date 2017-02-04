package org.twig4j.core.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.template.Template;

public class RuntimeTemplateCompilerTests {
    @Test
    public void testCompileJavaCode() throws Twig4jException {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler(new Environment());

        String sourceCode = "package org.twig4j.core.template;\n\n"
                + "public class TestTemplate extends org.twig4j.core.template.Template {\n"
                + "    protected String doDisplay(Context context, java.util.Map<String, TemplateBlockMethodSet> blocks) { return \"foo\"; }\n"
                + "    public String getTemplateName() { return \"foo\"; }\n"
                + "}\n";
        Template template = runtimeCompiler.compile(sourceCode, "org.twig4j.core.template.TestTemplate");

        Assert.assertEquals("Complied class method render() should return \"foo\"", "foo", template.render());
    }

    @Test(expected = Twig4jRuntimeException.class)
    public void testThrowsRuntimeErrorExceptionOnFailToCompile() throws Twig4jException {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler(new Environment());

        String sourceCode = "invalid java code";

        runtimeCompiler.compile(sourceCode, "something");
    }
}
