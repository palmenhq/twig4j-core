package org.twig.util;

import javax.script.ScriptException;
import java.util.ArrayList;
import java.util.List;

public class Php {
    public static String preg_match(Object regex, Object subject) {
        try {
        return (new com.caucho.quercus.script.QuercusScriptEngineFactory()).getScriptEngine()
                .eval("<?php return preg_match('" + String.valueOf(regex).replace("'", "\\'") + "', '" + String.valueOf(subject).replace("'", "\\'") + "');").toString();
        } catch (ScriptException e) {
            return "0";
        }
    }

    /**
     * Create a List with integers with default interval 1
     *
     * @param from Ie. 0
     * @param to Ie. 3
     * @return The range
     */
    public static List<Integer> range(Integer from, Integer to) {
        return range(from, to, 1);
    }

    /**
     * Create a List with integers with a specified interval
     *
     * @param from Ie. 0
     * @param to Ie. 30
     * @param interval ie 10
     * @return The range
     */
    public static List<Integer> range(Integer from, Integer to, Integer interval) {
        List<Integer> range = new ArrayList<>();

        if (from < to) {
            for (Integer i = from; i <= to; i += interval) {
                range.add(i);
            }
        } else {
            for (Integer i = from; i >= to; i -= interval) {
                range.add(i);
            }
        }

        return range;
    }

    public static List<String> range(String from, String to) {
        String lowerCaseLetters = "abcdefghijklmnopqrstuvxyz";
        String uppercaseLetters = "ABCDEFGHIJKLMNOPQRSTUVXYZ";
        String stringToWorkWith;
        Integer fromPosition;
        Integer toPosition;

        List<String> range = new ArrayList<>();

        if ((lowerCaseLetters.contains(from) && lowerCaseLetters.contains(to))) {
            stringToWorkWith = lowerCaseLetters;
        } else if (uppercaseLetters.contains(from) && uppercaseLetters.contains(to)) {
            stringToWorkWith = uppercaseLetters;
        } else if ((lowerCaseLetters.contains(from) && uppercaseLetters.contains(to)) || (lowerCaseLetters.contains(to) && uppercaseLetters.contains(from))) {
            throw new RuntimeException("Cannot mix upper- and lower-case letters in range");
        } else {
            throw new RuntimeException("Invalid characters specified for range");
        }

        fromPosition = stringToWorkWith.indexOf(from);
        toPosition = stringToWorkWith.indexOf(to);

        if (fromPosition < toPosition) {
            for (Integer i = fromPosition; i <= toPosition; i++) {
                range.add(stringToWorkWith.substring(i, i+1));
            }
        } else {
            for  (Integer i = fromPosition; i >= toPosition; i--) {
                range.add(stringToWorkWith.substring(i, i+1));
            }
        }

        return range;
    }
}
