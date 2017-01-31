package org.twigjava.syntax.operator;

public class BinaryAdd implements Operator {
    @Override
    public Integer getPrecedence() {
        return 30;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinaryAdd.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
