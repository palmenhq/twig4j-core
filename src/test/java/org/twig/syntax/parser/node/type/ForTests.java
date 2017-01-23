package org.twig.syntax.parser.node.type;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.type.expression.BinaryRange;
import org.twig.syntax.parser.node.type.expression.Constant;
import org.twig.syntax.parser.node.type.expression.Name;

public class ForTests {
    @Test
    // {% for key, val in 1..3 if true %}foo{% else %}bar{% endfor %}
    public void canCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        For.Settings settings = new For.Settings();
        settings
                .setKeyTarget("key")
                .setValueTarget("val")
                .setSeq(new BinaryRange(new Constant(1, 1), new Constant(3, 1), 1)) // 1..3
                .setIfExpr(new Constant(true, 1))
                .setBody(new Text("foo", 1))
                .setElseBody(new Text("bar", 1));

        For forNode = new For(settings, 1, "for");

        forNode.compile(compiler);

        // Just make sure the thing actually compiles and let the functional tests handle that the implementation works
        Assert.assertTrue(
                "Compiled code should have contents",
                compiler.getSourceCode().length() > 0
        );
    }
}
