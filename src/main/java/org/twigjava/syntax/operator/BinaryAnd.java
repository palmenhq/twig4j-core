package org.twigjava.syntax.operator;

public class BinaryAnd implements Operator {
    @Override
    public Integer getPrecedence() {
        return 15;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinaryAnd.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
