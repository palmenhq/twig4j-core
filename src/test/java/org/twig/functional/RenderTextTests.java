package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class RenderTextTests extends FunctionalTests {
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

    @Test
    public void canRenderVariables() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ bar }}");
        setupEnvironment(templates);

        HashMap<String, String> ctx = new HashMap<>();
        ctx.put("bar", "baz");

        Assert.assertEquals("Rendered variable should be equal to value", "baz", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderInterpolatedString() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ \"interpolated #{bar}\" }}");
        templates.put("bar.twig", "{{ 'not interpolated #{string}' }}");
        setupEnvironment(templates);

        HashMap<String, String> ctx = new HashMap<>();
        ctx.put("bar", "string");

        Assert.assertEquals(
                "Rendered double-quoted strings should be interpolated",
                "interpolated string",
                environment.render("foo.twig", ctx)
        );
        Assert.assertEquals(
                "Rendered single-quoted strings should not be interpolated",
                "not interpolated #{string}",
                environment.render("bar.twig")
        );
    }
}
