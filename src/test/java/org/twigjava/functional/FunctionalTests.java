package org.twigjava.functional;

import org.twigjava.Environment;
import org.twigjava.loader.HashMapLoader;

import java.util.HashMap;

public class FunctionalTests {
    protected Environment environment;

    protected void setupEnvironment(HashMap<String, String> templates) {
        environment = new Environment(new HashMapLoader(templates));

        environment.enableDebug();
        environment.enableStrictVariables();
    }
}
