package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigException;

import java.util.HashMap;

public class ExtendsTests extends FunctionalTests {
    @Test
    public void canExtendTemplateWithBlocks() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "{% extends 'bar.twigjava' %}\n" +
                "{% block a %}\n" +
                "foo\n" +
                "{% endblock %}"
        );
        templates.put("bar.twigjava", "bar {% block a %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "bar foo\n",
            environment.render("foo.twigjava")
        );
    }

    @Test
    public void canExtendTemplateWithBlocksInsideBlocks() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "{% extends 'bar.twigjava' %}\n" +
                "{% block b %}bar{% endblock %}"
        );
        templates.put("bar.twigjava", "{% block a %}foo {% block b %}{% endblock b %}{% endblock a %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo bar",
            environment.render("foo.twigjava")
        );
    }

    @Test
    public void canExtendTemplateWithSpacesAndNewlinesWithBlocks() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "\n   {% extends 'bar.twigjava' %} \n\n" +
                "{% block a %}\n" +
                "foo\n" +
                "{% endblock %}    \n\n"
        );
        templates.put("bar.twigjava", "bar {% block a %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "bar foo\n",
            environment.render("foo.twigjava")
        );
    }

    @Test(expected = SyntaxErrorException.class)
    public void throwsExceptionOnExtendingTemplateWithBody() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "{% extends 'bar.twigjava' %}\n" +
                "illegal body here {% block a %}{% endblock %}"
        );
        templates.put("bar.twigjava", "{% block a %}{% endblock %}");
        setupEnvironment(templates);

        environment.render("foo.twigjava");
    }

    @Test
    public void canDoMultipleExtends() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "{% extends 'bar.twigjava' %}\n" +
                "{% block a %}A{% endblock %}"
        );
        templates.put("bar.twigjava", "{% extends 'baz.twigjava' %}{% block a %}{% endblock %}{% block b %}B{% endblock %}");
        templates.put("baz.twigjava", "foo {% block a %}{% endblock %} {% block b %}{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo A B",
            environment.render("foo.twigjava")
        );
    }

    @Test
    public void canDoParent() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "{% extends 'bar.twigjava' %}\n" +
                "{% block a %}{{ parent() }}{% endblock %}"
        );
        templates.put("bar.twigjava", "foo {% block a %}parent A{% endblock %}");
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo parent A",
            environment.render("foo.twigjava")
        );
    }

    @Test
    public void canDoBlockReferenceFunction() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
            "foo.twigjava",
            "foo {% block a %}parent A{% endblock %}\n" +
                "{{ block('a') }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals(
            "Templates should be rendered correctly",
            "foo parent Aparent A",
            environment.render("foo.twigjava")
        );
    }
}
