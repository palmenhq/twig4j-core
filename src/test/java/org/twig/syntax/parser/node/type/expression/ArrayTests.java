package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;

import static org.mockito.Mockito.*;

public class ArrayTests {
    @Test
    public void canCompile() throws LoaderException {
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
