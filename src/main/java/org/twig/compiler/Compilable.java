package org.twig.compiler;

public interface Compilable {
    /**
     * Compile the instance to Java code
     * @param compiler The compiler to compile on
     */
    public void compile(ClassCompiler compiler);
}
