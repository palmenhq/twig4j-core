package org.twig.syntax.operator;

public class BinaryAdd implements Operator {
    @Override
    public Integer getPrecedence() {
        return 30;
    }

    @Override
    public Class getNodeClass() {
        return org.twig.syntax.parser.node.type.expression.BinaryAdd.class;
    }

    @Override
    public Associativity getAssociativity() {
        return Associativity.LEFT;
    }
}
