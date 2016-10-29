package org.twig.exception;

/**
 * Throws i.e. when failing to compile java code
 */
public class TwigRuntimeException extends TwigException {
    public TwigRuntimeException(String rawMessage, String templateName, Integer lineNumber) {
        super(rawMessage, templateName, lineNumber);
    }

    public TwigRuntimeException(String rawMessage, Throwable cause) {
        super(rawMessage);
        this.initCause(cause);
    }

    /**
     * Creates a new runtime exception for when a variable does not exist in context
     *
     * @param variable The variable name
     * @param templateName The template name
     * @return
     */
    public static TwigRuntimeException variableDoesNotExist(String variable, String templateName, Integer lineNumber) {
        return new TwigRuntimeException("Variable " + variable +  " does not exist.", templateName, lineNumber);
    }

    public static TwigRuntimeException badOperatorFailedNode(String operator, String templateName, Integer line, Throwable cause) {
        TwigRuntimeException e = new TwigRuntimeException("Bad operator \"" + operator + "\" (failed to instantiate binary node).", templateName, line);
        e.initCause(cause);

        return e;
    }
}
