package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryNullCoalesceTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(null, 1);
        Node right = new Constant(2, 1);
        BinaryNullCoalesce equalsNode = new BinaryNullCoalesce(left, right, 1);

        equalsNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should call compare method",
                "((null != null) ? null : 2)",
                compiler.getSourceCode()
        );
    }
}
