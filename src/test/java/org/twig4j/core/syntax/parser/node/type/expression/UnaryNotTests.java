package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class UnaryNotTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node node = new Constant(true, 1);
        UnaryNot notNode = new UnaryNot(node, 1);

        notNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should be an not expression",
                " !(new org.twig4j.core.typesystem.DynamicType(true)).toBoolean()",
                compiler.getSourceCode()
        );
    }
}
