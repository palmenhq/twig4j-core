package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryConcatTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(1, 1);
        Node right = new Constant(2, 2);
        BinaryConcat concatNode = new BinaryConcat(left, right, 1);

        concatNode.compile(compiler);

        Assert.assertEquals(
                "Code should be compiled correctly",
                "(String.valueOf(1).concat(String.valueOf(2)))",
                compiler.getSourceCode()
        );
    }
}
