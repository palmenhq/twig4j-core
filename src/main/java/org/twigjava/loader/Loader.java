package org.twigjava.loader;

import org.twigjava.exception.LoaderException;

/**
 * Interface all loaders must implement.
 */
public interface Loader {
    /**
     * Get the source code of a template, given it's name.
     *
     * @param name The name of the template to load
     *
     * @return The template source code
     *
     * @throws LoaderException If the template does not exist
     */
    public String getSource(String name) throws LoaderException;

    /**
     * Gets the cache key to use for the cache for a given template name.
     *
     * @param name The name of the template to load
     *
     * @return The cache key
     *
     * @throws LoaderException If the template does not exist
     */
    public String getCacheKey(String name) throws LoaderException;
}
