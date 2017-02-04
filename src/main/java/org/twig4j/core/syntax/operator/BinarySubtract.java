package org.twig4j.core.syntax.operator;

public class BinarySubtract implements Operator {
    @Override
    public Integer getPrecedence() {
        return 30;
    }

    @Override
    public Class getNodeClass() {
        return org.twig4j.core.syntax.parser.node.type.expression.BinarySubtract.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
