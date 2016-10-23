package org.twig.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.twig.template.Template;

public class RuntimeTemplateCompilerTests {
    @Test
    public void testCompileJavaCode() {
        RuntimeTemplateCompiler runtimeCompiler = new RuntimeTemplateCompiler();

        String sourceCode = "package org.twig.template;\n\n"
                + "public class RuntimeCompilerTestTemplate extends org.twig.template.Template {\n"
                + "    public String render() { return \"foo\"; }\n"
                + "}\n";
        Template template = runtimeCompiler.compile(sourceCode, "org.twig.template.RuntimeCompilerTestTemplate");

        Assert.assertEquals("Complied class method render() should return \"foo\"", "foo", template.render());
    }
}
