package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.LoaderException;
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
}
