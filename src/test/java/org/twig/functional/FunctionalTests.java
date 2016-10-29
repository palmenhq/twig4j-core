package org.twig.functional;

import org.twig.Environment;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class FunctionalTests {
    protected Environment environment;

    protected void setupEnvironment(HashMap<String, String> templates) {
        environment = new Environment(new HashMapLoader(templates));

        environment.enableDebug();
        environment.enableStrictVariables();
    }
}
