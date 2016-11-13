package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.syntax.parser.node.Node;

public class UnaryNot extends Unary {
    public UnaryNot(Node node, Integer line) {
        super(node, line);
    }

    @Override
    protected Unary compileOperator(ClassCompiler compiler) {
        compiler.writeRaw("!");

        return this;
    }
}
