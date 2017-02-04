package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.TwigException;

import java.util.HashMap;

public class FiltersTests extends FunctionalTests {
    @Test
    public void canApplyBasicFilters() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{{ 'foo'|upper }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should be upper cased", "FOO", environment.render("foo.twig4j"));
    }

    @Test
    public void canApplyMultipleFilters() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{{ 'Foo'|upper|lower }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should be lower cased", "foo", environment.render("foo.twig4j"));
    }

    @Test
    public void canApplyFiltersWithArguments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{{ ['foo', 'bar']|join(', ') }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should join array with provided argument", "foo, bar", environment.render("foo.twig4j"));
    }
}
