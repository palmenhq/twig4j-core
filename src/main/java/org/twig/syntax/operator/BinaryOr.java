package org.twig.syntax.operator;

public class BinaryOr implements Operator {
    @Override
    public Integer getPrecedence() {
        return 10;
    }

    @Override
    public Class getNodeClass() {
        return org.twig.syntax.parser.node.type.expression.BinaryOr.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
