package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;

public class NameTests {
    @Test
    public void canCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler classCompiler = new ClassCompiler(new Environment());
        Name nameNode = new Name("foo", 1);

        nameNode.compile(classCompiler);

        Assert.assertEquals(
                "Compiled source code should be correct",
                "getContext(context, \"foo\", false, 1)",
                classCompiler.getSourceCode()
        );
    }
}
