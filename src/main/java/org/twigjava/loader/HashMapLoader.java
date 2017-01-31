package org.twigjava.loader;

import org.twigjava.exception.LoaderException;

import java.util.HashMap;
import java.util.Map;

/**
 * Used for unit tests only
 */
public class HashMapLoader implements Loader {
    Map<String, String> hashMap;

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
    public HashMapLoader(Map<String, String> hashMap) {
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
    public Map<String, String> getHashMap() {
        return hashMap;
    }

    /**
     * Set the hashmap
     * @param hashMap The hashMap with templates
     */
    public void setHashMap(Map<String, String> hashMap) {
        this.hashMap = hashMap;
    }
}
