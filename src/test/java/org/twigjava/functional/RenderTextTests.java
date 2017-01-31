package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.TwigException;
import org.twigjava.template.Context;

import java.util.HashMap;

public class RenderTextTests extends FunctionalTests {
    @Test
    public void canRenderText() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "bar");
        setupEnvironment(templates);

        Assert.assertEquals("Text should be rendered properly", "bar", environment.render("foo.twigjava"));
    }

    @Test
    public void canRenderBasicPrintStatement() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ 'bar' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Rendered string should equal string contents", "bar", environment.render("foo.twigjava"));
    }

    @Test
    public void canRenderVariables() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ bar }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "baz");

        Assert.assertEquals("Rendered variable should be equal to value", "baz", environment.render("foo.twigjava", ctx));
    }

    @Test
    public void canRenderInterpolatedString() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ \"interpolated #{bar}\" }}");
        templates.put("bar.twigjava", "{{ 'not interpolated #{string}' }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("bar", "string");

        Assert.assertEquals(
                "Rendered double-quoted strings should be interpolated",
                "interpolated string",
                environment.render("foo.twigjava", ctx)
        );
        Assert.assertEquals(
                "Rendered single-quoted strings should not be interpolated",
                "not interpolated #{string}",
                environment.render("bar.twigjava")
        );
    }

    @Test
    public void canRenderConcat() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("concatStrings.twigjava", "{{ 'foo' ~ 'bar' }}");
        templates.put("concatNumbers.twigjava", "{{ 1 ~ 2 }}");
        templates.put("concatVariables.twigjava", "{{ 'foo' ~ var }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("var", "baz");

        Assert.assertEquals("Concatenated strings should be concatenated", "foobar", environment.render("concatStrings.twigjava"));
        Assert.assertEquals("Concatenated numbers should be concatenated and not added", "12", environment.render("concatNumbers.twigjava"));
        Assert.assertEquals("Concatenated strings+variables should be concatenated", "foobaz", environment.render("concatVariables.twigjava", ctx));
    }
}
