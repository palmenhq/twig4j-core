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

    /**
     * Gets the cache key to use for the cache for a given template name.
     *
     * @param name The name of the template to load
     * @return The cache key
     * @throws LoaderException When name is not found
     */
    public String getCacheKey(String name) throws LoaderException;
}
