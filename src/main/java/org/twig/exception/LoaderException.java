package org.twig.exception;

/**
 * Thrown by template loaders
 */
public class LoaderException extends TwigException {
    public LoaderException(String rawMessage) {
        super(rawMessage);
    }

    public static LoaderException notDefined(String name) {
        return new LoaderException(String.format("Template \"%s\" is not defined", name));
    }
}
