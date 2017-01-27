package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;

import java.util.HashMap;

public class IncludeTests extends FunctionalTests {
    @Test
    public void canIncludeTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "foo {% include('bar.twig') %}");
        templates.put("bar.twig", "bar");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Template should be included properly",
            "foo bar",
            environment.render("foo.twig")
        );
    }
}
