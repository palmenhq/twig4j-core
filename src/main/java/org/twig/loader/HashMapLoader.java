package org.twig.loader;

import org.twig.exception.LoaderException;

import java.util.HashMap;

/**
 * Used for unit tests only
 */
public class HashMapLoader implements Loader {
    HashMap<String, String> hashMap;

    /**
     * Default constructor
     */
    public HashMapLoader() {
        hashMap = new HashMap<>();
    }

    /**
     * Construct the loader with an existing hash map
     * @param hashMap The hashMap with templates
     */
    public HashMapLoader(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }

    @Override
    public String getSource(String name) throws LoaderException {
        if (hashMap.containsKey(name)) {
            return hashMap.get(name);
        }

        throw LoaderException.notDefined(name);
    }

    @Override
    public String getCacheKey(String name) throws LoaderException {
        if (!this.hashMap.containsKey(name)) {
            throw LoaderException.notDefined(name);
        }

        return this.hashMap.get(name);
    }

    /**
     * Get the hash map
     * @return The hashMap with templates
     */
    public HashMap<String, String> getHashMap() {
        return hashMap;
    }

    /**
     * Set the hashmap
     * @param hashMap The hashMap with templates
     */
    public void setHashMap(HashMap<String, String> hashMap) {
        this.hashMap = hashMap;
    }
}
