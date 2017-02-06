package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.template.Context;

import java.util.HashMap;

public class RenderMethodsTests extends FunctionalTests {
    @Test
    public void canRenderContextObjectMethods() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderMethodWithoutParenthesises() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test
    public void canRenderGetterFromPropertyName() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.something }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return value should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test
    public void canGetHashMapContents() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.something }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        HashMap<String, Object> object = new HashMap<>();
        object.put("something", "foo");
        ctx.put("foo", object);

        Assert.assertEquals("Hashmap's contents should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    @Test(expected = Twig4jRuntimeException.class)
    public void renderMethodOnNullVariableThrowsException() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test(expected = Twig4jRuntimeException.class)
    public void renderNonExistingMethodThrowsException() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.nonExistingMethod() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig", ctx);
    }

    @Test(expected = Twig4jRuntimeException.class)
    public void renderPrivateMethodThrowsException() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.privateMethod() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", new TestClass());

        environment.render("foo.twig", ctx);
    }

    @Test(expected = Twig4jRuntimeException.class)
    public void renderMethodThatThrowsExceptionThrowsException() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.methodThatThrowsException() }}");
        setupEnvironment(templates);

        Context ctx = new Context();
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
