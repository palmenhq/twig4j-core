package org.twigjava.compiler;

import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;

public interface Compilable {
    /**
     * Compile the instance to Java code
     * @param compiler The compiler to compile on
     * @throws LoaderException If a file with the file name cannot be found by loader
     */
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException;
}
