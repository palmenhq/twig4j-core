package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class UnaryNotTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node node = new Constant(true, 1);
        UnaryNot notNode = new UnaryNot(node, 1);

        notNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an not expression",
                " !(Boolean)true",
                compiler.getSourceCode()
        );
    }
}
