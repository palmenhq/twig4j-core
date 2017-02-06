package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class RenderTextTests extends FunctionalTests {
    @Test
    public void canRenderText() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "bar");
        setupEnvironment(templates);

        Assert.assertEquals("Text should be rendered properly", "bar", environment.render("foo.twig"));
    }

    @Test
    public void canRenderBasicPrintStatement() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ 'bar' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Rendered string should equal string contents", "bar", environment.render("foo.twig"));
    }

    @Test
    public void canRenderVariables() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals("Rendered variable should be equal to value", "baz", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderInterpolatedString() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ \"interpolated #{bar}\" }}");
        templates.put("bar.twig", "{{ 'not interpolated #{string}' }}");
        setupEnvironment(templates);

        Context ctx = new Context();
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

    @Test
    public void canRenderConcat() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("concatStrings.twig", "{{ 'foo' ~ 'bar' }}");
        templates.put("concatNumbers.twig", "{{ 1 ~ 2 }}");
        templates.put("concatVariables.twig", "{{ 'foo' ~ var }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("var", "baz");

        Assert.assertEquals("Concatenated strings should be concatenated", "foobar", environment.render("concatStrings.twig"));
        Assert.assertEquals("Concatenated numbers should be concatenated and not added", "12", environment.render("concatNumbers.twig"));
        Assert.assertEquals("Concatenated strings+variables should be concatenated", "foobaz", environment.render("concatVariables.twig", ctx));
    }

    @Test
    public void printNullRendersEmptyString() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("printNull.twig", "{{ null }}");

        setupEnvironment(templates);

        Assert.assertEquals("Print null should print empty string", "", environment.render("printNull.twig"));
    }
}
