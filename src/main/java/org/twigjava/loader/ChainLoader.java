package org.twigjava.loader;

import org.twigjava.exception.LoaderException;

import java.util.List;

public class ChainLoader implements Loader {
    List<Loader> loaders;

    public ChainLoader(List<Loader> loaders) {
        this.loaders = loaders;
    }

    @Override
    public String getSource(String name) throws LoaderException {
        Integer loaderIndex = 0;
        Integer numberOfLoaders = loaders.size();

        while (loaderIndex < numberOfLoaders) {
            try {
                return loaders.get(loaderIndex).getSource(name);
            } catch (LoaderException e) {
                // Just continue to the next loader
            }

            loaderIndex ++;
        }

        // No loader found any template
        throw LoaderException.notDefined(name);
    }

    @Override
    public String getCacheKey(String name) throws LoaderException {
        return getSource(name);
    }

    public List<Loader> getLoaders() {
        return loaders;
    }

    public ChainLoader setLoaders(List<Loader> loaders) {
        this.loaders = loaders;
        return this;
    }
}
