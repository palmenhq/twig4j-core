package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.Twig4jException;

import java.util.HashMap;

public class ExtendsTests extends FunctionalTests {
    @Test
    public void canExtendTemplateWithBlocks() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "{% extends 'bar.twig' %}\n" +
                "{% block a %}\n" +
                "foo\n" +
                "{% endblock %}"
        );
        templates.put("bar.twig", "bar {% block a %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "bar foo\n",
            environment.render("foo.twig")
        );
    }

    @Test
    public void canExtendTemplateWithBlocksInsideBlocks() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "{% extends 'bar.twig' %}\n" +
                "{% block b %}bar{% endblock %}"
        );
        templates.put("bar.twig", "{% block a %}foo {% block b %}{% endblock b %}{% endblock a %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo bar",
            environment.render("foo.twig")
        );
    }

    @Test
    public void canExtendTemplateWithSpacesAndNewlinesWithBlocks() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "\n   {% extends 'bar.twig' %} \n\n" +
                "{% block a %}\n" +
                "foo\n" +
                "{% endblock %}    \n\n"
        );
        templates.put("bar.twig", "bar {% block a %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "bar foo\n",
            environment.render("foo.twig")
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void throwsExceptionOnExtendingTemplateWithBody() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "{% extends 'bar.twig' %}\n" +
                "illegal body here {% block a %}{% endblock %}"
        );
        templates.put("bar.twig", "{% block a %}{% endblock %}");
        setupEnvironment(templates);

        environment.render("foo.twig");
    }

    @Test
    public void canDoMultipleExtends() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "{% extends 'bar.twig' %}\n" +
                "{% block a %}A{% endblock %}"
        );
        templates.put("bar.twig", "{% extends 'baz.twig' %}{% block a %}{% endblock %}{% block b %}B{% endblock %}");
        templates.put("baz.twig", "foo {% block a %}{% endblock %} {% block b %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo A B",
            environment.render("foo.twig")
        );
    }

    @Test
    public void canDoParent() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "{% extends 'bar.twig' %}\n" +
                "{% block a %}{{ parent() }}{% endblock %}"
        );
        templates.put("bar.twig", "foo {% block a %}parent A{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo parent A",
            environment.render("foo.twig")
        );
    }

    @Test
    public void canDoBlockReferenceFunction() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twig",
            "foo {% block a %}parent A{% endblock %}\n" +
                "{{ block('a') }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo parent Aparent A",
            environment.render("foo.twig")
        );
    }
}
