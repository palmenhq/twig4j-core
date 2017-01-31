package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.TwigException;

import java.util.HashMap;

public class ModifiersTests extends FunctionalTests {
    @Test
    public void canMakeSimpleAssignments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twigjava",
                "{% set foo = 'bar' %}\n" +
                    "{{ foo }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set variable should be rendered", "bar", environment.render("foo.twigjava"));
    }

    @Test
    public void canMakeComplexAssignments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twigjava",
                "{% set foo = {bar: 'baz'} %}\n" +
                    "{{ foo.bar }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set variable should be rendered", "baz", environment.render("foo.twigjava"));
    }

    @Test
    public void canMakeMultipleAssignments() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put(
                "foo.twigjava",
                "{% set foo, bar = 'baz', 'qux' %}\n" +
                    "{{ foo ~ bar }}"
        );
        setupEnvironment(templates);

        Assert.assertEquals("Contents of set both variables should be rendered", "bazqux", environment.render("foo.twigjava"));
    }

//  TODO capture assignments
//    @Test
//    public void canMakeCaptureAssignment() throws TwigException {
//        HashMap<String, String> templates = new HashMap<>();
//        templates.put(
//                "foo.twigjava",
//                "{% set foo %}bar{% endset %}\n" +
//                        "{{ foo }}"
//        );
//        setupEnvironment(templates);
//
//        Assert.assertEquals("Contents of set variable should be rendered", "bar", environment.render("foo.twigjava"));
//    }
}
