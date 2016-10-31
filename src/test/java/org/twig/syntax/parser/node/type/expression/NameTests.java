package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;

public class NameTests {
    @Test
    public void canCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler classCompiler = new ClassCompiler(new Environment());
        Name nameNode = new Name("foo", 1);

        nameNode.compile(classCompiler);

        Assert.assertEquals(
                "Compiled source code should be correct",
                "// line 1\n"
                + "getContext(context, \"foo\", false, 1)\n",
                classCompiler.getSourceCode()
        );
    }
}
