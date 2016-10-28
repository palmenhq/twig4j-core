package org.twig.compiler;

public interface LineAware {
    /**
     * For objects that are aware of their line number
     *
     * @return The line number
     */
    public Integer getLine();
}
