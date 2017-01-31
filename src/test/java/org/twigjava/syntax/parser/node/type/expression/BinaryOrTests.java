package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class BinaryOrTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(true, 1);
        Node right = new Constant(true, 1);
        BinaryOr orNode = new BinaryOr(left, right, 1);

        orNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an or expression",
                "(true || true)",
                compiler.getSourceCode()
        );
    }
}
