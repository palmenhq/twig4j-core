package org.twig4j.core.exception;

import org.twig4j.core.syntax.parser.node.Node;

/**
 * Throws i.e. when failing to compile java code
 */
public class Twig4jRuntimeException extends Twig4jException {
    public Twig4jRuntimeException(String rawMessage, String templateName, Integer lineNumber) {
        super(rawMessage, templateName, lineNumber);
    }

    public Twig4jRuntimeException(String rawMessage, String templateName, Integer lineNumber, Throwable e) {
        super(rawMessage, templateName, lineNumber);
        this.initCause(e);
    }

    public Twig4jRuntimeException(String rawMessage, Throwable cause) {
        super(rawMessage);
        this.initCause(cause);
    }

    /**
     * Creates a new runtime exception for when a variable does not exist in context
     *
     * @param variable The variable name
     * @param templateName The template name
     * @param lineNumber The line number
     *
     * @return The exception
     */
    public static Twig4jRuntimeException variableDoesNotExist(String variable, String templateName, Integer lineNumber) {
        return new Twig4jRuntimeException("Variable " + variable +  " does not exist.", templateName, lineNumber);
    }

    /**
     * Throw when trying to access an attribute that does not exist in a node
     *
     * @param attribute The attribute name
     * @param node The node
     * @param lineNumber The line number
     *
     * @return The exception
     */
    public static Twig4jRuntimeException nodeAttributeDoesNotExist(String attribute, Node node, Integer lineNumber) {
        return new Twig4jRuntimeException(
                String.format("Attribute \"%s\" does not exist for Node \"%s\".", attribute, node.getClass().getName()),
                null,
                lineNumber
        );
    }

    /**
     * When trying to get a node from another node when a node with that index does not exist
     *
     * @param index The node index
     * @param node The node trying to get a node from
     *
     * @return The exception
     */
    public static Twig4jRuntimeException noNodeWithIndexExists(Integer index, Node node) {
        return new Twig4jRuntimeException(
                String.format("Node \"%d\" does not exist for Node \"%s\".", index, node.getClass().getName()),
                null,
                node.getLine()
        );
    }

    /**
     * Throw when failing to instantiate an operator class (i.e. the class is badly configured)
     *
     * @param operator The operator name
     * @param templateName The template name
     * @param line The line the exception is thrown on
     * @param cause The cause of the exception
     *
     * @return The exception
     */
    public static Twig4jRuntimeException badOperatorFailedNode(String operator, String templateName, Integer line, Throwable cause) {
        Twig4jRuntimeException e = new Twig4jRuntimeException(
                "Bad operator \"" + operator + "\" (failed to instantiate binary node).",
                templateName,
                line
        );
        e.initCause(cause);

        return e;
    }

    /**
     * When trying to pop a state without a previous state
     *
     * @param templateName The template name
     * @param lineNumber The line number
     *
     * @return The exception
     */
    public static Twig4jRuntimeException popStateWithoutState(String templateName, Integer lineNumber) {
        return new Twig4jRuntimeException("Cannot pop state without a previous state", templateName, lineNumber);
    }

    /**
     * When trying to call an inaccessible method
     *
     * @param method The method name
     * @param className The class on which the method is called
     * @param templateName The template
     * @param e Previous exception
     *
     * @return The exception
     */
    public static Twig4jRuntimeException illegalAccessToMethod(String method, String className, String templateName, Throwable e) {
        return new Twig4jRuntimeException(
                "Call to inaccessible method \"" + method + "\" on object of type \"" + className + "\"",
                templateName,
                -1,
                e
        );
    }

    /**
     * When an exception is thrown during the invocation of a method
     *
     * @param method The method that threw an exception
     * @param className The class the method belongs to
     * @param templateName The template
     * @param e Previous exception
     *
     * @return The exception
     */
    public static Twig4jRuntimeException invocationTargetException(String method, String className, String templateName, Throwable e) {
        return new Twig4jRuntimeException(
                "Method \"" + className + "#" + method + "()\" threw exception " + e.getCause().getClass().getName() + " \"" + e.getCause().getMessage() + "\"",
                templateName,
                -1,
                e.getCause()
        );
    }
}
