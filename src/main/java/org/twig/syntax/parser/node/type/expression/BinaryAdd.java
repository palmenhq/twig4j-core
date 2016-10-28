package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.syntax.parser.node.Node;

public class BinaryAdd extends Binary {
    public BinaryAdd(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        compiler.writeRaw(" + ");

        return this;
    }
}
