package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryInTests {
    @Test
    public void testCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant("foo", 1);
        Node right = new Constant("foobar", 2);
        BinaryIn inNode = new BinaryIn(left, right, 1);

        inNode.compile(compiler);

        Assert.assertEquals(
                "Code should be compiled correctly",
                "(org.twig4j.core.extension.Core.inFilter(\"foo\", \"foobar\"))",
                compiler.getSourceCode()
        );
    }
}
