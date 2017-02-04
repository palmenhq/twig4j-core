package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.syntax.parser.node.Node;

public class UnaryNot extends Unary {
    public UnaryNot(Node node, Integer line) {
        super(node, line);
    }

    @Override
    protected Unary compileOperator(ClassCompiler compiler) {
        compiler.writeRaw("!(Boolean)");

        return this;
    }
}
