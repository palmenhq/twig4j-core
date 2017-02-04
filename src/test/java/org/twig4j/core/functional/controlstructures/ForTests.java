package org.twig4j.core.functional.controlstructures;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.Twig4jException;
import org.twig4j.core.functional.FunctionalTests;

import java.util.HashMap;

public class ForTests extends FunctionalTests {
    @Test
    public void canRenderForInRange() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for foo in 'a'..'c' %}\n" +
                        "{{ foo }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Full range should be rendered",
                "a\nb\nc\n",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canRenderArray() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for item in ['foo', 'bar'] %}{{ item }} {% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "All array keys should be rendered",
                "foo bar ",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canRenderArrayWithKeys() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for key, item in ['foo', 'bar'] %}{{ key }}: {{ item }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "All array keys should be rendered",
                "0: foo\n1: bar\n",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canUseLoopVariable() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for item in ['foo', 'bar'] %}\n" +
                        "{{ loop.index }} {{ loop.index0 }} {{ loop.first }} {{ loop.revindex }} {{ loop.revindex0 }} {{ loop.last }} {{ item }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Loop variables should be rendered correctly",
                "1 0 true 2 1 false foo\n" +
                        "2 1 false 1 0 true bar\n",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canDoLoopInsideLoop() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
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
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canLoopHashMaps() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for key, item in { foo: 'bar', baz: 'qux' } %}\n" +
                        "{{ key }}: {{ item }}\n" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Key and items should be rendered correctly",
                "foo: bar\n" +
                    "baz: qux\n",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canLoopWithIf() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for i in 1..5 if i % 2 == 1 %}\n" +
                        "{{ i }} " +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Only items that matches if condition should be rendered",
                "1 3 5 ",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void canUseElseBody() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for i in [] %}\n" +
                        "do nothing here\n" +
                        "{% else %}\n" +
                        "else works" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Only \"else works\" text should be printed",
                "else works",
                environment.render("foo.twig4j")
        );
    }

    @Test
    public void doesNotTriggerElseBodyWhenIterated() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% for i in [1, 2] %}\n" +
                        "{{ i }}" +
                        "{% else %}\n" +
                        "Don't print this" +
                        "{% endfor %}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
                "Else text should not be printed",
                "12",
                environment.render("foo.twig4j")
        );
    }
}