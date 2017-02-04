package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryLessThanOrEqualTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(5, 1);
        Node right = new Constant(2, 1);
        BinaryLessThanOrEqual lteqNode = new BinaryLessThanOrEqual(left, right, 1);

        lteqNode.compile(compiler);

        Assert.assertEquals(
                "Complied source should be less than or equal",
                "(Double.valueOf(5) <= Double.valueOf(2))",
                compiler.getSourceCode()
        );
    }
}
