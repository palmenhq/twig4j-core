package org.twig.extension;

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
}
