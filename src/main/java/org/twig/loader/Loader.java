package org.twig.loader;

import org.twig.exception.LoaderException;

/**
 * Interface all loaders must implement.
 */
public interface Loader {
    /**
     * Get the source code of a template, given it's name.
     *
     * @param name The name of the template to load
     * @return The template source code
     */
    public String getSource(String name) throws LoaderException;
}
