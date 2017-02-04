package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class RenderTextTests extends FunctionalTests {
    @Test
    public void canRenderText() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "bar");
        setupEnvironment(templates);

        Assert.assertEquals("Text should be rendered properly", "bar", environment.render("foo.twig4j"));
    }

    @Test
    public void canRenderBasicPrintStatement() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ 'bar' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Rendered string should equal string contents", "bar", environment.render("foo.twig4j"));
    }

    @Test
    public void canRenderVariables() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals("Rendered variable should be equal to value", "baz", environment.render("foo.twig4j", ctx));
    }

    @Test
    public void canRenderInterpolatedString() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ \"interpolated #{bar}\" }}");
        templates.put("bar.twig4j", "{{ 'not interpolated #{string}' }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "string");

        Assert.assertEquals(
                "Rendered double-quoted strings should be interpolated",
                "interpolated string",
                environment.render("foo.twig4j", ctx)
        );
        Assert.assertEquals(
                "Rendered single-quoted strings should not be interpolated",
                "not interpolated #{string}",
                environment.render("bar.twig4j")
        );
    }

    @Test
    public void canRenderConcat() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("concatStrings.twig4j", "{{ 'foo' ~ 'bar' }}");
        templates.put("concatNumbers.twig4j", "{{ 1 ~ 2 }}");
        templates.put("concatVariables.twig4j", "{{ 'foo' ~ var }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("var", "baz");

        Assert.assertEquals("Concatenated strings should be concatenated", "foobar", environment.render("concatStrings.twig4j"));
        Assert.assertEquals("Concatenated numbers should be concatenated and not added", "12", environment.render("concatNumbers.twig4j"));
        Assert.assertEquals("Concatenated strings+variables should be concatenated", "foobaz", environment.render("concatVariables.twig4j", ctx));
    }
}
