package org.twig.extension;

import org.twig.syntax.operator.Operator;

import java.util.HashMap;

public interface Extension {
    /**
     * Get unary operators of this extension
     * @return A hashmap of operators indexed by their operator string
     */
    public HashMap<String, Operator> getUnaryOperators();

    /**
     * Get binary operators of this extension
     * @return A hashmap of operators indexed by their operator string
     */
    public HashMap<String, Operator> getBinaryOperators();

    /**
     * Get the name of the extension
     * @return The name
     */
    public String getName();
}
