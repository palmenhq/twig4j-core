package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryAddTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());

        Node left = new Constant(1, 1);
        Node right = new Constant(1, 1);
        BinaryAdd addNode = new BinaryAdd(left, right, 1);

        addNode.compile(compiler);

        Assert.assertEquals(
            "Compiled source should add 2 dynamic types",
            "((new org.twig4j.core.typesystem.DynamicType(1)).add(new org.twig4j.core.typesystem.DynamicType(1)))",
            compiler.getSourceCode()
        );
    }
}
