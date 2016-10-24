package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class RenderTests {
    private Environment environment;

    @Test
    public void canRenderText() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "bar");
        setupEnvironment(templates);

        Assert.assertEquals("Text should be rendered properly", "bar", environment.render("foo.twig"));
    }

    @Test
    public void canRenderBasicPrintStatement() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ 'bar' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Rendered string should equal string contents", "bar", environment.render("foo.twig"));
    }

    private void setupEnvironment(HashMap<String, String> templates) {
        environment = new Environment(new HashMapLoader(templates));
    }
}
