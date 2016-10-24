package org.twig.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.twig.template.Template;

public class RuntimeTemplateCompilerTests {
    @Test
    public void testCompileJavaCode() {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler();

        String sourceCode = "package org.twig.template;\n\n"
                + "public class TestTemplate extends org.twig.template.Template {\n"
                + "    protected String doRender(java.util.HashMap<String, String> context) { return \"foo\"; }\n"
                + "}\n";
        Template template = runtimeCompiler.compile(sourceCode, "org.twig.template.TestTemplate");

        Assert.assertEquals("Complied class method render() should return \"foo\"", "foo", template.render());
    }
}
