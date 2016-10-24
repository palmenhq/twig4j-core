package org.twig.compiler;

import org.twig.Environment;
import org.twig.exception.LoaderException;

public class ClassCompiler {
    private int numberOfIndents = 0;
    private StringBuilder sourceCode = new StringBuilder();
    private Environment environment;

    /**
     * @param environment The Twig environment
     */
    public ClassCompiler(Environment environment) {
        this.environment = environment;
    }

    /**
     * Add an indention and compile the passed node
     *
     * @param node The node to compile
     * @return Itself
     */
    public ClassCompiler subCompile(Compilable node) throws LoaderException {
        indent();

        node.compile(this);

        return this;
    }

    /**
     * Compiles the passed node
     * @param node The node to compile
     * @return Itself
     * @throws LoaderException
     */
    public ClassCompiler compile(Compilable node) throws LoaderException {
        node.compile(this);

        return this;
    }

    /**
     * Write source code with a trailing line break
     *
     * @param code The code to write
     * @return this
     */
    public ClassCompiler writeLine(String code) {
        this.writeIndent();
        sourceCode
                .append(code)
                .append("\n");

        return this;
    }

    /**
     * Write source code
     *
     * @param code The code to write
     * @return
     */
    public ClassCompiler write(String code) {
        this.writeIndent();
        sourceCode.append(code);

        return this;
    }

    /**
     * Increases the indention level by 1
     *
     * @return this
     */
    public ClassCompiler writeIndent() {
        for (int i = 0; i < numberOfIndents; i++) {
            // One indent = 4 spaces
            sourceCode.append("    ");
        }

        return this;
    }
    public ClassCompiler indent() {
        numberOfIndents++;

        return this;
    }


    /**
     * Decreases the indention level by 1
     *
     * @return this
     */
    public ClassCompiler unIndent() {
        if (numberOfIndents > 0) {
            numberOfIndents--;
        }

        return this;
    }

    /**
     * Get the compiled source code
     *
     * @return the compiled source code
     */
    public String getSourceCode() {
        return sourceCode.toString();
    }

    /**
     * @return The Twig environment
     */
    public Environment getEnvironment() {
        return environment;
    }

    /**
     * @param environment The Twig environment
     */
    public ClassCompiler setEnvironment(Environment environment) {
        this.environment = environment;

        return this;
    }
}
