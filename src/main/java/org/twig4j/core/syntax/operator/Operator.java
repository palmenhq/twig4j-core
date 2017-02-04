package org.twig4j.core.syntax.operator;

public interface Operator {
    public static enum Associativity { LEFT, RIGHT }

    /**
     * Get the precedence ("importance"/weight/order) of the operator
     * @return The precedence
     */
    public Integer getPrecedence();

    /**
     * Get the class of the operator node to instantiate
     * @return The class
     */
    public Class getNodeClass();

    /**
     * Get the associativity (left or right)
     * @return The type of associativity
     */
    public Associativity getAssociativity();
}
