package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.TwigException;

import java.util.HashMap;

public class FiltersTests extends FunctionalTests {
    @Test
    public void canApplyBasicFilters() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twigjava",
                "{{ 'foo'|upper }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should be upper cased", "FOO", environment.render("foo.twigjava"));
    }

    @Test
    public void canApplyMultipleFilters() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twigjava",
                "{{ 'Foo'|upper|lower }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should be lower cased", "foo", environment.render("foo.twigjava"));
    }

    @Test
    public void canApplyFiltersWithArguments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twigjava",
                "{{ ['foo', 'bar']|join(', ') }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("applied filter should join array with provided argument", "foo, bar", environment.render("foo.twigjava"));
    }
}
