package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.template.Context;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class RenderDataStructuresTests extends FunctionalTests {
    @Test
    public void canRenderArray() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ ['foo', 'bar', 'baz'][1] }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "2nd element of array should be rendered",
                "bar",
                environment.render("foo.twig4j")
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void cantRenderArrayAccessToUndefinedKey() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ ['foo', 'bar', 'baz'][5] }}");
        setupEnvironment(templates);

        environment.render("foo.twig4j");
    }

    @Test
    public void canRenderArrayFromContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo[0] }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        List<String> list = new ArrayList<>();
        list.add("bar");
        ctx.put("foo", list);

        Assert.assertEquals(
                "Content of called array element should be rendered",
                "bar",
                environment.render("foo.twig4j", ctx)
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void cantRenderArrayAccessToNonArray() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ foo[0] }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", 1);

        environment.render("foo.twig4j", ctx);
    }

    @Test
    public void canRenderHashmap() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ ({foo: { 'bar': 'qux' } }).foo.bar }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "Contents of key inside hashmap inside hashmap should be rendered",
                "qux",
                environment.render("foo.twig4j")
        );
    }
}
