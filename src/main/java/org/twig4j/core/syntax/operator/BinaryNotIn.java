package org.twig4j.core.syntax.operator;

public class BinaryNotIn implements Operator {
    @Override
    public Integer getPrecedence() {
        return 20;
    }

    @Override
    public Class getNodeClass() {
        return org.twig4j.core.syntax.parser.node.type.expression.BinaryNotIn.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}