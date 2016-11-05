package org.twig.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryStartsWithTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new StringConstant("foobar", 1);
        Node right = new StringConstant("foo", 1);
        BinaryStartsWith startsWithNode = new BinaryStartsWith(left, right, 1);

        startsWithNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should check if string starts with",
                "(String.valueOf(\"foobar\").startsWith(String.valueOf(\"foo\")))",
                compiler.getSourceCode()
        );
    }
}
