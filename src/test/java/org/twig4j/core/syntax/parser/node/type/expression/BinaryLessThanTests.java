package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryLessThanTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant(5, 1);
        Node right = new Constant(2, 1);
        BinaryLessThan ltNode = new BinaryLessThan(left, right, 1);

        ltNode.compile(compiler);

        Assert.assertEquals(
                "Complied source should be less than",
                "((new org.twig4j.core.typesystem.DynamicType(5)).compareTo((new org.twig4j.core.typesystem.DynamicType(2))) < 0)",
                compiler.getSourceCode()
        );
    }
}
