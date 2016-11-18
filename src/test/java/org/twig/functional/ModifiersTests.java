package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigException;

import java.util.HashMap;

public class ModifiersTests extends FunctionalTests {
    @Test
    public void canMakeSimpleAssignments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% set foo = 'bar' %}\n" +
                    "{{ foo }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set variable should be rendered", "bar", environment.render("foo.twig"));
    }

    @Test
    public void canMakeComplexAssignments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% set foo = {bar: 'baz'} %}\n" +
                    "{{ foo.bar }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set variable should be rendered", "baz", environment.render("foo.twig"));
    }

    @Test
    public void canMakeMultipleAssignments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig",
                "{% set foo, bar = 'baz', 'qux' %}\n" +
                    "{{ foo ~ bar }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set both variables should be rendered", "bazqux", environment.render("foo.twig"));
    }

//  TODO capture assignments
//    @Test
//    public void canMakeCaptureAssignment() throws TwigException {
//        HashMap<String, String> templates = new HashMap<>();
//        templates.put(
//                "foo.twig",
//                "{% set foo %}bar{% endset %}\n" +
//                        "{{ foo }}"
//        );
//        setupEnvironment(templates);
//
//        Assert.assertEquals("Contents of set variable should be rendered", "bar", environment.render("foo.twig"));
//    }
}
