package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class RenderMethodsTests extends FunctionalTests {
    @Test
    public void canRenderContextObjectMethods() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig4j", ctx));
    }

    @Test
    public void canRenderMethodWithoutParenthesises() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.getSomething }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig4j", ctx));
    }

    @Test
    public void canRenderGetterFromPropertyName() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.something }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig4j", ctx));
    }

    @Test
    public void canGetHashMapContents() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.something }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        HashMap<String, Object> object = new HashMap<>();
        object.put("something", "foo");
        ctx.put("foo", object);

        Assert.assertEquals("Hashmap's contents should be rendered", "foo", environment.render("foo.twig4j", ctx));
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderMethodOnNullVariableThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        environment.render("foo.twig4j");
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderNonExistingMethodThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.nonExistingMethod() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig4j", ctx);
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderPrivateMethodThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.privateMethod() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig4j", ctx);
    }

    @Test(expected = TwigRuntimeException.class)
    public void renderMethodThatThrowsExceptionThrowsException() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo.methodThatThrowsException() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig4j", ctx);
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
