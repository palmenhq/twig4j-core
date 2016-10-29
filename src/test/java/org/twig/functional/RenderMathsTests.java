package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class RenderMathsTests extends FunctionalTests {
    @Test
    public void canRenderNumber() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ 1134.12341 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1134.12341 should be rendered", "1134.12341", environment.render("foo.twig"));
    }

    @Test
    public void canDoAddition() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("addition.twig", "{{ 1 + 1 }}");
        templates.put("multiple-addition.twig", "{{ 1 + 2+3+4 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 + 1 should be 2", "2", environment.render("addition.twig"));
        Assert.assertEquals("1 + 2 + 3 + 4 should be 10", "10", environment.render("multiple-addition.twig"));
    }
}
