package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class RenderMathsTests {
    private Environment environment;

    @Test
    public void canRenderNumber() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ 112341234.12341234 }}");
        setupEnvironment(templates);

        Assert.assertEquals("112341234.12341234 should be rendered", "112341234.12341234", environment.render("foo.twig"));
    }

    @Test
    public void canDoAddition() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ 1 + 1 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 + 1 should be 2", "2", environment.render("foo.twig"));
    }

    private void setupEnvironment(HashMap<String, String> templates) {
        environment = new Environment(new HashMapLoader(templates));

        environment.enableDebug();
        environment.enableStrictVariables();
    }
}
