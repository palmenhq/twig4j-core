package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

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
