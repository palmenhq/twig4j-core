package org.twig.compiler;

import org.twig.exception.LoaderException;

public interface Compilable {
    /**
     * Compile the instance to Java code
     * @param compiler The compiler to compile on
     * @throws LoaderException If a file with the file name cannot be found by loader
     */
    public void compile(ClassCompiler compiler) throws LoaderException;
}
