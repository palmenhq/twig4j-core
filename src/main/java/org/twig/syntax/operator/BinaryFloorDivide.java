package org.twig.syntax.operator;

public class BinaryFloorDivide implements Operator {
    @Override
    public Integer getPrecedence() {
        return 60;
    }

    @Override
    public Class getNodeClass() {
        return org.twig.syntax.parser.node.type.expression.BinaryFloorDivide.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}