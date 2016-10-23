package org.twig.compiler;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;

public class ClassCompilerTests {
    @Test
    public void canWriteAndGetSourceCode() {
        ClassCompiler classCompiler = new ClassCompiler(new Environment());
        String code = "import com.twig.something;";
        classCompiler.write(code);

        Assert.assertEquals("Written code is equal to written source code", code, classCompiler.getSourceCode());

        // Start fresh since we dont want the old source code
        classCompiler = new ClassCompiler(new Environment());
        classCompiler.writeLine(code);

        Assert.assertEquals("Written line should end with newline", code.concat("\n"), classCompiler.getSourceCode());
    }

    @Test
    public void canIndent() {
        ClassCompiler classCompiler = new ClassCompiler(new Environment());
        String code = "import com.twig.something";

        classCompiler
                .indent()
                .write(code);

        Assert.assertEquals("Indented code is equal to 4 spaces + written line", "    ".concat(code), classCompiler.getSourceCode());

        // Start fresh since we dont want the old source code
        classCompiler = new ClassCompiler(new Environment());
        classCompiler
                .indent()
                .unIndent()
                .write(code);

        Assert.assertEquals("Indented and then unIndented code is equal to written line", code, classCompiler.getSourceCode());
    }
}
