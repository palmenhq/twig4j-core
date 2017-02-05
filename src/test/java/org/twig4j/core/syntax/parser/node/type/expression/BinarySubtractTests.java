package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

import static org.mockito.Mockito.*;

public class BinarySubtractTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(5, 1);
        Node right = new Constant(2, 1);
        BinarySubtract subtractNode = new BinarySubtract(left, right, 1);

        subtractNode.compile(compiler);

        Assert.assertEquals(
            "Compiled source should subtract 2 dynamic types",
            "((new org.twig4j.core.typesystem.DynamicType(5)).subtract(new org.twig4j.core.typesystem.DynamicType(2)))",
            compiler.getSourceCode()
        );

    }
}
