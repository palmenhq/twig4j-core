package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

import static org.mockito.Mockito.*;

public class BinaryDivideTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(5, 1);
        Node right = new Constant(2, 1);
        BinaryDivide divideNode = new BinaryDivide(left, right, 1);

        divideNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an expression that handles doubles/ints",
                "(((double)5) / 2)",
                compiler.getSourceCode()
        );
    }
}
