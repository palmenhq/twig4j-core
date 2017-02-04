package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class IncludeTests extends FunctionalTests {
    @Test
    public void canIncludeTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "foo {% include('bar.twig4j') %}");
        templates.put("bar.twig4j", "bar");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Template should be included properly",
            "foo bar",
            environment.render("foo.twig4j")
        );
    }

    @Test
    public void canIncludeTemplateWithContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "foo {% include('bar.twig4j') %}");
        templates.put("bar.twig4j", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals(
            "Template should be included properly with context",
            "foo baz",
            environment.render("foo.twig4j", ctx)
        );
    }

    @Test
    public void canIncludeTemplateWithContextAndWith() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "foo {% include('bar.twig4j') with { qux: 'quux' } %}");
        templates.put("bar.twig4j", "{{ bar }} {{ qux }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals(
            "Template should be included properly with context",
            "foo baz quux",
            environment.render("foo.twig4j", ctx)
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void includeWithOnlyThrowsErrorOnAccessToParentContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "foo {% include('bar.twig4j') with { qux: 'quux' } only %}");
        templates.put("bar.twig4j", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        environment.render("foo.twig4j", ctx);
    }

    @Test(expected = LoaderException.class)
    public void cantIncludeNonExistingTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{% include('nonExistingTemplate') %}");
        setupEnvironment(templates);

        environment.render("foo.twig4j");
    }

    @Test
    public void ignoresMissing() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "foo {% include('nonExisting.twig4j') ignore missing %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Template should render properly without exception",
            "foo ",
            environment.render("foo.twig4j")
        );
    }
}
