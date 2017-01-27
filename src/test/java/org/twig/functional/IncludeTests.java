package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.template.Context;

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

    @Test
    public void canIncludeTemplateWithContext() throws TwigException {
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
    public void canIncludeTemplateWithContextAndWith() throws TwigException {
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

    @Test(expected = TwigRuntimeException.class)
    public void includeWithOnlyThrowsErrorOnAccessToParentContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "foo {% include('bar.twig') with { qux: 'quux' } only %}");
        templates.put("bar.twig", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        environment.render("foo.twig", ctx);
    }

    @Test(expected = LoaderException.class)
    public void cantIncludeNonExistingTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{% include('nonExistingTemplate') %}");
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test
    public void ignoresMissing() throws TwigException {
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
