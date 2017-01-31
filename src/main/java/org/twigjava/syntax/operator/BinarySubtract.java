package org.twigjava.syntax.operator;

public class BinarySubtract implements Operator {
    @Override
    public Integer getPrecedence() {
        return 30;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinarySubtract.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
