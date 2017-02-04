package org.twig4j.core.util;

public class HashMap extends java.util.LinkedHashMap<String, Object> {
    /**
     * Put a value to the HashMap and return itself
     *
     * @param key The entity key
     * @param value The entity
     *
     * @return This
     */
    public HashMap put(String key, Object value) {
        super.put(key, value);

        return this;
    }
}
