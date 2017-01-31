package org.twigjava.syntax.operator;

public class BinaryLessThanOrEqual implements Operator {
    @Override
    public Integer getPrecedence() {
        return 20;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinaryLessThanOrEqual.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
