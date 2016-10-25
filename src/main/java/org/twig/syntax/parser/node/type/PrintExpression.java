package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.Expression;

public class PrintExpression extends Node {
    public PrintExpression(Expression expression, Integer line) {
        super(line);
        addNode(expression);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
        compiler
                .write("output = output.concat(")
                .compile(getExpression())
                .writeRaw(");\n");
    }

    public Expression getExpression() {
        return (Expression)nodes.get(0);
    }
}
