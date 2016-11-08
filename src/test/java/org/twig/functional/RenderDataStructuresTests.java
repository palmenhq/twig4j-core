package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RenderDataStructuresTests extends FunctionalTests {
    @Test
    public void canRenderArray() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ ['foo', 'bar', 'baz'][1] }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "2nd element of array should be rendered",
                "bar",
                environment.render("foo.twig")
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void cantRenderArrayAccessToUndefinedKey() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ ['foo', 'bar', 'baz'][5] }}");
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test
    public void canRenderArrayFromContext() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo[0] }}");
        setupEnvironment(templates);

        Map<String, List<String>> ctx = new HashMap<>();
        List<String> list = new ArrayList<>();
        list.add("bar");
        ctx.put("foo", list);

        Assert.assertEquals(
                "Content of called array element should be rendered",
                "bar",
                environment.render("foo.twig", ctx)
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void cantRenderArrayAccessToNonArray() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo[0] }}");
        setupEnvironment(templates);

        Map<String, Integer> ctx = new HashMap<>();
        ctx.put("foo", 1);

        environment.render("foo.twig", ctx);
    }

    @Test
    public void canRenderHashmap() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ ({foo: 'bar': { baz: 'qux' } }).foo.bar }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "Contents of key inside hashmap inside hashmap should be rendered",
                "qux",
                environment.render("foo.twig")
        );
    }
}
