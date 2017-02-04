package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryEndsWithTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new StringConstant("foobar", 1);
        Node right = new StringConstant("bar", 1);
        BinaryEndsWith endsWithNode = new BinaryEndsWith(left, right, 1);

        endsWithNode.compile(compiler);

        Assert.assertEquals(
                "Compiled source should check if string ends with",
                "(String.valueOf(\"foobar\").endsWith(String.valueOf(\"bar\")))",
                compiler.getSourceCode()
        );
    }
}
