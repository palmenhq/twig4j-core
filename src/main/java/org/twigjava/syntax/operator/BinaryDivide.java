package org.twigjava.syntax.operator;

public class BinaryDivide implements Operator {
    @Override
    public Integer getPrecedence() {
        return 60;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinaryDivide.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
