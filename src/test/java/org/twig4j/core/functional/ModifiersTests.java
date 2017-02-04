package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.Twig4jException;

import java.util.HashMap;

public class ModifiersTests extends FunctionalTests {
    @Test
    public void canMakeSimpleAssignments() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% set foo = 'bar' %}\n" +
                    "{{ foo }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set variable should be rendered", "bar", environment.render("foo.twig4j"));
    }

    @Test
    public void canMakeComplexAssignments() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% set foo = {bar: 'baz'} %}\n" +
                    "{{ foo.bar }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set variable should be rendered", "baz", environment.render("foo.twig4j"));
    }

    @Test
    public void canMakeMultipleAssignments() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twig4j",
                "{% set foo, bar = 'baz', 'qux' %}\n" +
                    "{{ foo ~ bar }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set both variables should be rendered", "bazqux", environment.render("foo.twig4j"));
    }

//  TODO capture assignments
//    @Test
//    public void canMakeCaptureAssignment() throws Twig4jException {
//        HashMap<String, String> templates = new HashMap<>();
//        templates.put(
//                "foo.twig4j",
//                "{% set foo %}bar{% endset %}\n" +
//                        "{{ foo }}"
//        );
//        setupEnvironment(templates);
//
//        Assert.assertEquals("Contents of set variable should be rendered", "bar", environment.render("foo.twig4j"));
//    }
}
