package org.twigjava.syntax.parser.node.type;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.TwigException;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.type.expression.Constant;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class SetTests {
    @Test
    public void canCompileSimple() throws TwigException {
        ClassCompiler classCompiler = new ClassCompiler(new Environment());

        List<String> names = Arrays.asList("foo");
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Constant("bar", 1));
        Node node = new Node(nodes, new HashMap<>(), 1, null);
        // {% set foo = "bar" %}
        Set set = new Set(names, node, false, 1, "foo");

        set.compile(classCompiler);

        Assert.assertEquals(
                "Should compile simple assign statements",
                "// line 1\n" +
                    "((java.util.Map<String, Object>)context).put(\"foo\", \"bar\");\n",
                classCompiler.getSourceCode()
        );
    }

    @Test
    public void canCompileMultipleAssignment() throws TwigException {
        ClassCompiler classCompiler = new ClassCompiler(new Environment());

        List<String> names = Arrays.asList("foo", "bar");
        List<Node> nodes = new ArrayList<>();
        nodes.add(new Constant("baz", 1));
        nodes.add(new Constant("qux", 1));
        Node node = new Node(nodes, new HashMap<>(), 1, null);
        // {% set foo, bar = "baz", "qux" %}
        Set set = new Set(names, node, false, 1, "foo");

        set.compile(classCompiler);

        Assert.assertEquals(
                "Should compile multiple assign statements",
                "// line 1\n" +
                        "((java.util.Map<String, Object>)context).put(\"foo\", \"baz\");\n" +
                        "((java.util.Map<String, Object>)context).put(\"bar\", \"qux\");\n",
                classCompiler.getSourceCode()
        );
    }
}
