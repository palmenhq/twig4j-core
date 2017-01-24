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
}