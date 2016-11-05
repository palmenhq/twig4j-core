package org.twig.util;

import javax.script.ScriptException;

public class Php {
    public static String preg_match(Object regex, Object subject) {
        try {
        return (new com.caucho.quercus.script.QuercusScriptEngineFactory()).getScriptEngine()
                .eval("<?php return preg_match('" + String.valueOf(regex).replace("'", "\\'") + "', '" + String.valueOf(subject).replace("'", "\\'") + "');").toString();
        } catch (ScriptException e) {
            return "0";
        }
    }
}
