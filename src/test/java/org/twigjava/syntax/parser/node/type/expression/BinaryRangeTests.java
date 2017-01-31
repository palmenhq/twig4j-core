package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class BinaryRangeTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(1, 1);
        Node right = new Constant(3, 2);
        BinaryRange rangeNode = new BinaryRange(left, right, 1);

        rangeNode.compile(compiler);

        Assert.assertEquals(
                "Code should be compiled correctly",
                "(org.twigjava.util.Php.range(1, 3))",
                compiler.getSourceCode()
        );
    }
}
