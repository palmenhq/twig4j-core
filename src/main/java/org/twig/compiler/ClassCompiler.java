package org.twig.compiler;

import org.twig.Environment;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;

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
    public ClassCompiler subCompile(Compilable node) throws LoaderException, TwigRuntimeException {
        indent();

        node.compile(this);

        unIndent();

        return this;
    }

    /**
     * Compiles the passed node
     * @param node The node to compile
     * @return Itself
     * @throws LoaderException
     */
    public ClassCompiler compile(Compilable node) throws LoaderException, TwigRuntimeException {
        // Reset compiler
        sourceCode = new StringBuilder();
        numberOfIndents = 0;

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

    /**
     * Adds a raw string to the compiled code.
     *
     * @param value Something to write
     * @return this
     */
    public ClassCompiler writeRaw(String value) {
        sourceCode.append(value);

        return this;
    }

    /**
     * Write the provided string as native java or a string (ie. true instead of "true", but "aoeu" not aoeu)
     * @param value The value to write
     * @return this
     */
    public ClassCompiler representValue(String value) {
        if (value.matches("^(\\d+)$")) {
            writeRaw(value);

            return this;
        }

        if (value.matches("^[\\d\\.]+$")) {
            writeRaw(value + "d");

            return this;
        }

        if (value.equals("true") || value.equals("false") || value.equals("null")) {
            writeRaw(value);

            return this;
        }

        // If nothing else matches this value it's a string
        this.writeString(value);

        return this;
    }

    /**
     * Write a string
     *
     * @param text The string to write
     * @return this
     */
    public ClassCompiler writeString(String text) {
        sourceCode.append("\"" + escapeString(text) + "\"");

        return this;
    }

    /**
     * Escapes the provided string to work in java string
     *
     * @param text The stirng to escape
     * @return The escaped string
     */
    private String escapeString(String text) {
        return text.replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\"", "\\\"");
    }

    public ClassCompiler addDebugInfo(LineAware node) {
        writeLine("// line " + node.getLine());

        return this;
    }

    /**
     * Increase the number of indents
     * @return this
     */
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
