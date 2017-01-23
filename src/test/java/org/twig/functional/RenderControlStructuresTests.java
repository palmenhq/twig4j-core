package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderControlStructuresTests extends FunctionalTests {
    @Test
    public void canRenderIf() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if foo %}foo{% endif %}\n"
                + "{% if bar %}bar{% endif %}\n"
        );
        setupEnvironment(templates);

        HashMap<String, Boolean> ctx = new HashMap<>();
        ctx.put("foo", true);
        ctx.put("bar", false);

        Assert.assertEquals(
                "Only contents of true if statement should be rendered",
                "foo",
                environment.render("foo.twig", ctx)
        );
    }

    @Test
    public void canRenderElse() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if foo %}foo{% else %}bar{% endif %}\n"
        );
        setupEnvironment(templates);

        HashMap<String, Boolean> ctx = new HashMap<>();
        ctx.put("foo", false);

        Assert.assertEquals(
                "Contents of else statement should be rendered",
                "bar",
                environment.render("foo.twig", ctx)
        );
    }

    @Test
    public void canRenderElseIf() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if foo %}\n" +
                        "foo\n"
                + "{% elseif bar %}bar{% endif %}\n"
        );
        setupEnvironment(templates);

        HashMap<String, Boolean> ctx = new HashMap<>();
        ctx.put("foo", false);
        ctx.put("bar", true);

        Assert.assertEquals(
                "Contents of elseif statement should be rendered",
                "bar",
                environment.render("foo.twig", ctx)
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void cantRenderUnclosedIf() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if true %}"
        );
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test
    public void canRenderForInRange() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% for foo in 'a'..'c' %}\n" +
                        "{{ foo }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Contents of range should be rendered",
                "a\nb\nc\n",
                environment.render("foo.twig")
        );
    }
}
