package org.twig4j.core.syntax.operator;

public class BinaryOr implements Operator {
    @Override
    public Integer getPrecedence() {
        return 10;
    }

    @Override
    public Class getNodeClass() {
        return org.twig4j.core.syntax.parser.node.type.expression.BinaryOr.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
