package org.twigjava.syntax.operator;

public class BinaryRange implements Operator {
    @Override
    public Integer getPrecedence() {
        return 25;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinaryRange.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
