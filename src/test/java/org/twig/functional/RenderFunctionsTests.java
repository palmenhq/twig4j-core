package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class RenderFunctionsTests extends FunctionalTests {
    @Test
    public void canRenderContextObjectMethods() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderMethodWithoutParenthesises() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderGetterFromPropertyName() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.something }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test
    public void canGetHashMapContents() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.something }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        HashMap<String, Object> object = new HashMap<>();
        object.put("something", "foo");
        ctx.put("foo", object);

        Assert.assertEquals("Hashmap's contents should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderMethodOnNullVariableThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderNonExistingMethodThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.nonExistingMethod() }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig", ctx);
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderPrivateMethodThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.privateMethod() }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig", ctx);
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderMethodThatThrowsExceptionThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.methodThatThrowsException() }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig", ctx);
    }

    public class TestClass {
        public String getSomething() {
            return "foo";
        }

        private String privateMethod() { return "foo"; }

        public String methodThatThrowsException() throws Exception {
            throw new Exception("Something went wrong");
        }
    }
}
