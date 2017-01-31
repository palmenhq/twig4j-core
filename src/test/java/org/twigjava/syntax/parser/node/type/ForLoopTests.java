package org.twigjava.syntax.parser.node.type;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;

public class ForLoopTests {
    @Test
    public void canCompile() throws LoaderException, TwigRuntimeException {
        ForLoop forLoop = new ForLoop(1, "for");

        ClassCompiler compiler = new ClassCompiler(new Environment());

        forLoop.compile(compiler);

        // Proper tests made with functional tests
        Assert.assertTrue("Code should be compiled", compiler.getSourceCode().length() > 0);
    }
}
