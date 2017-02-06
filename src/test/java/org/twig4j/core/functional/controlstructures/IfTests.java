package org.twig4j.core.functional.controlstructures;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.functional.FunctionalTests;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class IfTests extends FunctionalTests {
    @Test
    public void canRenderIf() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if foo %}foo{% endif %}\n"
                        + "{% if bar %}bar{% endif %}\n"
        );
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", true);
        ctx.put("bar", false);

        Assert.assertEquals(
                "Only contents of true if statement should be rendered",
                "foo",
                environment.render("foo.twig", ctx)
        );
    }

    @Test
    public void canRenderElse() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if foo %}foo{% else %}bar{% endif %}\n"
        );
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", false);

        Assert.assertEquals(
                "Contents of else statement should be rendered",
                "bar",
                environment.render("foo.twig", ctx)
        );
    }

    @Test
    public void canRenderElseIf() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if foo %}\n" +
                        "foo\n"
                        + "{% elseif bar %}bar{% endif %}\n"
        );
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", false);
        ctx.put("bar", true);

        Assert.assertEquals(
                "Contents of elseif statement should be rendered",
                "bar",
                environment.render("foo.twig", ctx)
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void cantRenderUnclosedIf() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% if true %}"
        );
        setupEnvironment(templates);

        environment.render("foo.twig");
    }
}
