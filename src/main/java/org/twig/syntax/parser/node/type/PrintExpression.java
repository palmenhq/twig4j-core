package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.Expression;

public class PrintExpression extends Node {
    public PrintExpression(Node expression, Integer line) {
        super(line);
        addNode(expression);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
        compiler
                .writeLine("output = output.concat(")
                .indent()
                    .compile(getExpression())
                .unIndent()
                .writeLine(");");
    }

    public Node getExpression() {
        return nodes.get(0);
    }
}
