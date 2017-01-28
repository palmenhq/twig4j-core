package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;

import java.util.HashMap;

public class FiltersTests extends FunctionalTests {
    @Test
    public void canApplyBasicFilters() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{{ 'foo'|upper }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should be upper cased", "FOO", environment.render("foo.twig"));
    }
    @Test
    public void canApplyMultipleFilters() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{{ 'Foo'|upper|lower }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should be lower cased", "foo", environment.render("foo.twig"));
    }
}
