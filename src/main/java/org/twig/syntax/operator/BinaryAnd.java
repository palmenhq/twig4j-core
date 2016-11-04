package org.twig.syntax.operator;

public class BinaryAnd implements Operator {
    @Override
    public Integer getPrecedence() {
        return 15;
    }

    @Override
    public Class getNodeClass() {
        return org.twig.syntax.parser.node.type.expression.BinaryAnd.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
