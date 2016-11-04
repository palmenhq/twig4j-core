package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;

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
    public void canCompareBoolVars() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo == false }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", false);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig", ctx));
    }

    @Test
    public void canCompareStringVars() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo == \"bar\" }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", "bar");

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig", ctx));
    }

    @Test(expected = TwigRuntimeException.class)
    public void cantCompareDifferentTypesWithStrictTypesEnabled() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo == 1 }}");
        setupEnvironment(templates);
        environment.enableStrictTypes();

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", "1");

        environment.render("foo.twig", ctx);
    }

    @Test
    public void canRenderNotComparedBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ true != false }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twig"));
    }

    @Test
    public void canDoOr() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ false or true }}");
        setupEnvironment(templates);

        Assert.assertEquals("\"false or true\" should be true", "true", environment.render("foo.twig"));
    }

    @Test
    public void canDoAnd() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ false and true }}");
        setupEnvironment(templates);

        Assert.assertEquals("\"false and true\" should be false", "false", environment.render("foo.twig"));
    }
}
