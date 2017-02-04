package org.twig4j.core.compiler;

public interface LineAware {
    /**
     * For objects that are aware of their line number
     *
     * @return The line number
     */
    public Integer getLine();
}
