package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryInTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant("foo", 1);
        Node right = new Constant("foobar", 2);
        BinaryIn rangeNode = new BinaryIn(left, right, 1);

        rangeNode.compile(compiler);

        Assert.assertEquals(
                "Code should be compiled correctly",
                "(org.twig.extension.Core.inFilter(\"foo\", \"foobar\"))",
                compiler.getSourceCode()
        );
    }
}
