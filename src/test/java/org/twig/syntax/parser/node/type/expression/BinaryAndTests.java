package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryAndTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant("true", 1);
        Node right = new Constant("true", 1);
        BinaryAnd orNode = new BinaryAnd(left, right, 1);

        orNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should ",
                "(true && true)",
                compiler.getSourceCode()
        );
    }
}
