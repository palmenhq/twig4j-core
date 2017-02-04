package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;

public class NameTests {
    @Test
    public void canCompile() throws LoaderException, Twig4jRuntimeException {
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
