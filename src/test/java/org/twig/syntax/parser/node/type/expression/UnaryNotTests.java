package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class UnaryNotTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node node = new Constant(true, 1);
        UnaryNot notNode = new UnaryNot(node, 1);

        notNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an not expression",
                " !true",
                compiler.getSourceCode()
        );
    }
}
