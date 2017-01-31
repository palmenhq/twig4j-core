package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.template.Context;

import java.util.HashMap;

public class IncludeTests extends FunctionalTests {
    @Test
    public void canIncludeTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "foo {% include('bar.twigjava') %}");
        templates.put("bar.twigjava", "bar");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Template should be included properly",
            "foo bar",
            environment.render("foo.twigjava")
        );
    }

    @Test
    public void canIncludeTemplateWithContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "foo {% include('bar.twigjava') %}");
        templates.put("bar.twigjava", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals(
            "Template should be included properly with context",
            "foo baz",
            environment.render("foo.twigjava", ctx)
        );
    }

    @Test
    public void canIncludeTemplateWithContextAndWith() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "foo {% include('bar.twigjava') with { qux: 'quux' } %}");
        templates.put("bar.twigjava", "{{ bar }} {{ qux }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals(
            "Template should be included properly with context",
            "foo baz quux",
            environment.render("foo.twigjava", ctx)
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void includeWithOnlyThrowsErrorOnAccessToParentContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "foo {% include('bar.twigjava') with { qux: 'quux' } only %}");
        templates.put("bar.twigjava", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        environment.render("foo.twigjava", ctx);
    }

    @Test(expected = LoaderException.class)
    public void cantIncludeNonExistingTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{% include('nonExistingTemplate') %}");
        setupEnvironment(templates);

        environment.render("foo.twigjava");
    }

    @Test
    public void ignoresMissing() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "foo {% include('nonExisting.twigjava') ignore missing %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Template should render properly without exception",
            "foo ",
            environment.render("foo.twigjava")
        );
    }
}
