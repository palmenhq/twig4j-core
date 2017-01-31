package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.syntax.parser.node.Node;

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
