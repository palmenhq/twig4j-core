package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class IncludeTests extends FunctionalTests {
    @Test
    public void canIncludeTemplate() throws Twig4jException {
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

    @Test
    public void canIncludeTemplateWithContext() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "foo {% include('bar.twig') %}");
        templates.put("bar.twig", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals(
            "Template should be included properly with context",
            "foo baz",
            environment.render("foo.twig", ctx)
        );
    }

    @Test
    public void canIncludeTemplateWithContextAndWith() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "foo {% include('bar.twig') with { qux: 'quux' } %}");
        templates.put("bar.twig", "{{ bar }} {{ qux }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals(
            "Template should be included properly with context",
            "foo baz quux",
            environment.render("foo.twig", ctx)
        );
    }

    @Test(expected = Twig4jRuntimeException.class)
    public void includeWithOnlyThrowsErrorOnAccessToParentContext() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "foo {% include('bar.twig') with { qux: 'quux' } only %}");
        templates.put("bar.twig", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        environment.render("foo.twig", ctx);
    }

    @Test(expected = LoaderException.class)
    public void cantIncludeNonExistingTemplate() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{% include('nonExistingTemplate') %}");
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test
    public void ignoresMissing() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "foo {% include('nonExisting.twig') ignore missing %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Template should render properly without exception",
            "foo ",
            environment.render("foo.twig")
        );
    }
}
