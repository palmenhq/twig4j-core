package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryAndTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(true, 1);
        Node right = new Constant(true, 1);
        BinaryAnd orNode = new BinaryAnd(left, right, 1);

        orNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an and expression",
                "(true && true)",
                compiler.getSourceCode()
        );
    }
}
