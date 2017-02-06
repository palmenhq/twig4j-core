package org.twig4j.core.syntax.parser.node.type.control;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.type.control.ForLoop;

public class ForLoopTests {
    @Test
    public void canCompile() throws LoaderException, Twig4jRuntimeException {
        ForLoop forLoop = new ForLoop(1, "for");

        ClassCompiler compiler = new ClassCompiler(new Environment());

        forLoop.compile(compiler);

        // Proper tests made with functional tests
        Assert.assertTrue("Code should be compiled", compiler.getSourceCode().length() > 0);
    }
}
