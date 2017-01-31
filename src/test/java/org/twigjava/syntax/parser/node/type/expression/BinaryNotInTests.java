package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class BinaryNotInTests {
    @Test
    public void testCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Node left = new Constant("foo", 1);
        Node right = new Constant("foobar", 2);
        BinaryNotIn notInNode = new BinaryNotIn(left, right, 1);

        notInNode.compile(compiler);

        Assert.assertEquals(
                "Code should be compiled correctly",
                "(! (org.twigjava.extension.Core.inFilter(\"foo\", \"foobar\")) )",
                compiler.getSourceCode()
        );
    }
}
