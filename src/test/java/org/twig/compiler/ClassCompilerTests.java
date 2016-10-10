package org.twig.compiler;

import org.junit.Assert;
import org.junit.Test;

public class ClassCompilerTests {
    @Test
    public void canWriteAndGetSourceCode() {
        ClassCompiler classCompiler = new ClassCompiler();
        String code = "import com.twig.something;";
        classCompiler.write(code);

        Assert.assertEquals("Written code is equal to written source code", code, classCompiler.getSourceCode());

        // Start fresh since we dont want the old source code
        classCompiler = new ClassCompiler();
        classCompiler.writeLine(code);

        Assert.assertEquals("Written line should end with newline", code.concat("\n"), classCompiler.getSourceCode());
    }

    @Test
    public void canIndent() {
        ClassCompiler classCompiler = new ClassCompiler();
        String code = "import com.twig.something";

        classCompiler
                .indent()
                .write(code);

        Assert.assertEquals("Indented code is equal to 4 spaces + written line", "    ".concat(code), classCompiler.getSourceCode());

        // Start fresh since we dont want the old source code
        classCompiler = new ClassCompiler();
        classCompiler
                .indent()
                .unIndent()
                .write(code);

        Assert.assertEquals("Indented and then unIndented code is equal to written line", code, classCompiler.getSourceCode());
    }
}
