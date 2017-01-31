package org.twigjava.syntax.operator;

public class UnaryNot implements Operator {
    @Override
    public Integer getPrecedence() {
        return 50;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.UnaryNot.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.RIGHT;
    }
}
