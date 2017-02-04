package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;

public class ArrayTests {
    @Test
    public void canCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());

        Array array = new Array(1);
        array.addNode(new Constant("Foo", 1));
        array.addNode(new Constant("Bar", 1));

        array.compile(compiler);

        Assert.assertEquals(
                "Compiled source code should be valid java array",
                "java.util.Arrays.asList(\"Foo\", \"Bar\")",
                compiler.getSourceCode()
        );
    }
}
