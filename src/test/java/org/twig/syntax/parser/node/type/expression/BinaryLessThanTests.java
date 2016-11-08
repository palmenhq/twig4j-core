package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryLessThanTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(5, 1);
        Node right = new Constant(2, 1);
        BinaryLessThan ltNode = new BinaryLessThan(left, right, 1);

        ltNode.compile(compiler);

        Assert.assertEquals(
                "Complied source should be less than",
                "(5 < 2)",
                compiler.getSourceCode()
        );
    }
}
