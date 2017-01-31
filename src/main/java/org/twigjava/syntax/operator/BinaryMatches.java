package org.twigjava.syntax.operator;

public class BinaryMatches implements Operator {
    @Override
    public Integer getPrecedence() {
        return 20;
    }

    @Override
    public Class getNodeClass() {
        return org.twigjava.syntax.parser.node.type.expression.BinaryMatches.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
