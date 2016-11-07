package org.twig.syntax.operator;

public class BinaryPowerOf implements Operator {
    @Override
    public Integer getPrecedence() {
        return 200;
    }

    @Override
    public Class getNodeClass() {
        return org.twig.syntax.parser.node.type.expression.BinaryPowerOf.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.RIGHT;
    }
}
