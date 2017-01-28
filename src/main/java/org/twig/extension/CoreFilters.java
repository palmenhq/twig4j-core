package org.twig.extension;

import java.util.List;

public class CoreFilters {
    /**
     * Uppercase a string
     *
     * @param string
     * @return
     */
    public static String upper(String string) {
        return string.toUpperCase();
    }

    /**
     * Lowercase a string
     *
     * @param string
     * @return
     */
    public static String lower(String string) {
        return string.toLowerCase();
    }

    /**
     * Join an array with a string
     *
     * @param array The array to join
     * @param glue The "glue"
     * @return
     */
    public static String join(List<String> array, String glue) {
        return String.join(glue, array);
    }
}
