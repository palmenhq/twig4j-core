package org.twig.functional.controlstructures;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;
import org.twig.functional.FunctionalTests;

import java.util.HashMap;

public class ForTests extends FunctionalTests {
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
                "Full range should be rendered",
                "a\nb\nc\n",
                environment.render("foo.twig")
        );
    }

    @Test
    public void canRenderArray() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% for item in ['foo', 'bar'] %}{{ item }} {% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "All array keys should be rendered",
                "foo bar ",
                environment.render("foo.twig")
        );
    }

    @Test
    public void canRenderArrayWithKeys() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% for key, item in ['foo', 'bar'] %}{{ key }}: {{ item }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "All array keys should be rendered",
                "0: foo\n1: bar\n",
                environment.render("foo.twig")
        );
    }

    @Test
    public void canUseLoopVariable() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% for item in ['foo', 'bar'] %}\n" +
                        "{{ loop.index }} {{ loop.index0 }} {{ loop.first }} {{ item }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Loop variables should be rendered correctly",
                "1 0 true foo\n" +
                        "2 1 false bar\n",
                environment.render("foo.twig")
        );

    }

    @Test
    public void canDoLoopInsideLoop() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% for key1, item1 in ['foo', 'bar'] %}\n" +
                        "{{ key1 }} {{ item1 }} {{ loop.index }} {{ loop.index0 }} {{ loop.first }}\n" +
                        "{% for key2, item2 in ['baz', 'qux'] %}\n" +
                            "{{ key2 }} {{ item2 }} {{ loop.index }} {{ loop.index0 }} {{ loop.first }}\n" +
                        "{% endfor %}{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Loop variables should be rendered correctly",
                // Loop 1:1
                "0 foo 1 0 true\n" +
                // Loop 2:1
                "0 baz 1 0 true\n" +
                // Loop 2:2
                "1 qux 2 1 false\n" +
                // Loop 1:2
                "1 bar 2 1 false\n" +
                // Loop 2:1
                "0 baz 1 0 true\n" +
                // Loop 2:2
                "1 qux 2 1 false\n",
                environment.render("foo.twig")
        );

    }

    @Test
    public void canLoopHashMaps() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% for key, item in { foo: 'bar', baz: 'qux' } %}\n" +
                        "{{ key }}: {{ item }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Key and items should be rendered correctly",
                "foo: bar\n" +
                    "baz: qux\n",
                environment.render("foo.twig")
        );

    }
}