package org.twig.exception;

/**
 * Thrown by template loaders
 */
public class LoaderException extends TwigException {
    public LoaderException(String rawMessage) {
        super(rawMessage);
    }
}
