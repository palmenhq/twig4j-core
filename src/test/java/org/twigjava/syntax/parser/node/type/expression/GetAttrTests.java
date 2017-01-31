package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.TwigException;

public class GetAttrTests {
    @Test
    public void canCompileMethod() throws TwigException {
        Expression node = new Name("foo", 1);
        Expression attribute = new Constant("bar", 1);;
        Expression arguments = new Array(1);
        arguments.addNode(new Constant("baz", 1));
        arguments.addNode(new Name("qux", 1));
        GetAttr getAttr = new GetAttr(node, attribute, arguments, "method", 1);
        ClassCompiler compiler = new ClassCompiler(new Environment());

        getAttr.compile(compiler);

        String compiledSource = compiler.getSourceCode();

        // It's really hard to check the whole string because of the debug comments but this should somehow resemble the outputs
        // which without the debug thingies would be this:
        // getAttribute(getContext(context, "foo" ...), "bar", Arrays.asList("baz", getContext(context, "qux" ...)), "method")
        Assert.assertTrue(
                "Compiled source should be a call to the getAttribute method",
                compiledSource.contains("getAttribute(")
        );
        Assert.assertTrue(
                "getAttribute should be called with getContext(context, foo ...)",
                compiledSource.contains("getContext(context, \"foo\"")
        );
        Assert.assertTrue(
                "getAttribute should be called with the Arrays.asList",
                compiledSource.contains("Arrays.asList(")
        );
    }
}
