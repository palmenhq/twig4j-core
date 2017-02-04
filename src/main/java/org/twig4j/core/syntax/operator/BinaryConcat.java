package org.twig4j.core.syntax.operator;

public class BinaryConcat implements Operator {
    @Override
    public Integer getPrecedence() {
        return 40;
    }

    @Override
    public Class getNodeClass() {
        return org.twig4j.core.syntax.parser.node.type.expression.BinaryConcat.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
