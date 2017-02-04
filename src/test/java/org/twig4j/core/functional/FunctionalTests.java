package org.twig4j.core.functional;

import org.twig4j.core.Environment;
import org.twig4j.core.loader.HashMapLoader;

import java.util.HashMap;

public class FunctionalTests {
    protected Environment environment;

    protected void setupEnvironment(HashMap<String, String> templates) {
        environment = new Environment(new HashMapLoader(templates));

        environment.enableDebug();
        environment.enableStrictVariables();
    }
}
