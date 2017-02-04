package org.twig4j.core.syntax.parser.node.type.control;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.type.Text;
import org.twig4j.core.syntax.parser.node.type.expression.Constant;

import java.util.ArrayList;

public class IfStatementTests {
    @Test
    public void canCompile() throws TwigException {
        ArrayList<Node> contents = new ArrayList<>();
        contents.add(new IfBody(new Constant(true, 1), new Text("foo", 1), 1));
        contents.add(new ElseIfBody(new Constant(false, 2), new Text("bar", 2), 2));
        contents.add(new ElseBody(new Text("baz", 2), 3));

        IfStatement ifStatement = new IfStatement(contents, 1, "if");
        ClassCompiler compiler = new ClassCompiler(new Environment());

        ifStatement.compile(compiler);

        String source = compiler.getSourceCode();
        Assert.assertTrue(
                "Should contain if statement with true",
                source.contains("if ((Boolean) true) {")
        );
        Assert.assertTrue(
                "Should contain elseif statement with false",
                source.contains("} else if ((Boolean) false) {")
        );
        Assert.assertTrue(
                "Should contain else statement",
                source.contains("} else {")
        );
    }
}
