package org.twig4j.core.compiler;

import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;

public interface Compilable {
    /**
     * Compile the instance to Java code
     *
     * @param compiler The compiler to compile on
     *
     * @throws LoaderException If a file with the file name cannot be found by loader
     * @throws Twig4jRuntimeException If runtime errors are encountered
     */
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException;
}
