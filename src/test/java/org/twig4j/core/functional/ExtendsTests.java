package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.TwigException;

import java.util.HashMap;

public class ExtendsTests extends FunctionalTests {
    @Test
    public void canExtendTemplateWithBlocks() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "{% extends 'bar.twig4j' %}\n" +
                "{% block a %}\n" +
                "foo\n" +
                "{% endblock %}"
        );
        templates.put("bar.twig4j", "bar {% block a %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "bar foo\n",
            environment.render("foo.twig4j")
        );
    }

    @Test
    public void canExtendTemplateWithBlocksInsideBlocks() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "{% extends 'bar.twig4j' %}\n" +
                "{% block b %}bar{% endblock %}"
        );
        templates.put("bar.twig4j", "{% block a %}foo {% block b %}{% endblock b %}{% endblock a %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo bar",
            environment.render("foo.twig4j")
        );
    }

    @Test
    public void canExtendTemplateWithSpacesAndNewlinesWithBlocks() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "\n   {% extends 'bar.twig4j' %} \n\n" +
                "{% block a %}\n" +
                "foo\n" +
                "{% endblock %}    \n\n"
        );
        templates.put("bar.twig4j", "bar {% block a %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "bar foo\n",
            environment.render("foo.twig4j")
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void throwsExceptionOnExtendingTemplateWithBody() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "{% extends 'bar.twig4j' %}\n" +
                "illegal body here {% block a %}{% endblock %}"
        );
        templates.put("bar.twig4j", "{% block a %}{% endblock %}");
        setupEnvironment(templates);

        environment.render("foo.twig4j");
    }

    @Test
    public void canDoMultipleExtends() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "{% extends 'bar.twig4j' %}\n" +
                "{% block a %}A{% endblock %}"
        );
        templates.put("bar.twig4j", "{% extends 'baz.twig4j' %}{% block a %}{% endblock %}{% block b %}B{% endblock %}");
        templates.put("baz.twig4j", "foo {% block a %}{% endblock %} {% block b %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo A B",
            environment.render("foo.twig4j")
        );
    }

    @Test
    public void canDoParent() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "{% extends 'bar.twig4j' %}\n" +
                "{% block a %}{{ parent() }}{% endblock %}"
        );
        templates.put("bar.twig4j", "foo {% block a %}parent A{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo parent A",
            environment.render("foo.twig4j")
        );
    }

    @Test
    public void canDoBlockReferenceFunction() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig4j",
            "foo {% block a %}parent A{% endblock %}\n" +
                "{{ block('a') }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo parent Aparent A",
            environment.render("foo.twig4j")
        );
    }
}
