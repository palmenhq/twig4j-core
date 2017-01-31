package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class BinaryPowerOfTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(5, 1);
        Node right = new Constant(2, 1);
        BinaryPowerOf powNode = new BinaryPowerOf(left, right, 1);

        powNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an expression that handles power of",
                "(Math.pow(5, 2))",
                compiler.getSourceCode()
        );
    }
}
