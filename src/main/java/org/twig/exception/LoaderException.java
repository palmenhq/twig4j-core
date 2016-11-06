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

    public static LoaderException directoryDoesNotExist(String directory) {
        return new LoaderException(String.format("The directory \"%s\" does not exist.", directory));
    }

    public static LoaderException nullByteName() {
        return new LoaderException("A template name cannot contain NUL bytes.");
    }

    public static LoaderException outsideConfiguredDirectories(String name) {
        return new LoaderException(String.format("Looks like you try to load a template outside configured directories (%s).", name));
    }

    public static LoaderException malformedNamespacedTemplateName(String name) {
        return new LoaderException(String.format("Malformed namespaced template name \"%s\" (expecting \"@namespace/template_name\").", name));
    }
}
