package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.LoaderException;
import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.template.Context;

import java.util.HashMap;

public class ExtendsTests extends FunctionalTests {
    @Test
    public void canExtendTemplateWithBlocks() throws TwigException {
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

    @Test(expected = SyntaxErrorException.class)
    public void throwsExceptionOnExtendingTemplateWithBody() throws TwigException {
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
    public void canDoMultipleExtends() throws TwigException {
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
}
