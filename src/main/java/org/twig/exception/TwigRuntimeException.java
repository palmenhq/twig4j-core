package org.twig.exception;

/**
 * Throws i.e. when failing to compile java code
 */
public class TwigRuntimeException extends TwigException {
    public TwigRuntimeException(String rawMessage) {
        super(rawMessage);
    }

    public TwigRuntimeException(String rawMessage, Throwable cause) {
        super(rawMessage);
        this.initCause(cause);
    }
}
