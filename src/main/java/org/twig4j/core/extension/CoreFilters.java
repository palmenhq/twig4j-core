package org.twig4j.core.extension;

import java.util.List;

public class CoreFilters {
    /**
     * Uppercase a string
     *
     * @param string The string to uppercase
     *
     * @return The upper-cased string
     */
    public static String upper(String string) {
        return string.toUpperCase();
    }

    /**
     * Lowercase a string
     *
     * @param string The string to lowercase
     *
     * @return The lower-cased string
     */
    public static String lower(String string) {
        return string.toLowerCase();
    }

    /**
     * Join an array with a string
     *
     * @param array The array to join
     * @param glue The "glue"
     *
     * @return The joined string
     */
    public static String join(List<String> array, String glue) {
        return String.join(glue, array);
    }
}
