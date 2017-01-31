package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.syntax.parser.node.Node;

public class BinaryLessThan extends Binary {
    public BinaryLessThan(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        compiler.writeRaw("<");

        return this;
    }
}
