package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;

import java.util.HashMap;

public class RenderLogicTests extends FunctionalTests {
    @Test
    public void canRenderPlainBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ true }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig"));
    }

    @Test
    public void canRenderComparedBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ true == false }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "false", environment.render("foo.twig"));
    }

    @Test
    public void canRenderComparedBoolVars() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo == false }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", false);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderComparedStringVars() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo == \"bar\" }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", "bar");

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderNotComparedBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ true != false }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig"));
    }
}
