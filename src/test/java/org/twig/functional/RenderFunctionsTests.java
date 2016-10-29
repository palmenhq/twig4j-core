package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.loader.HashMapLoader;

import java.util.HashMap;

public class RenderFunctionsTests extends FunctionalTests {
    @Test
    public void canRenderContextObjectMethods() throws TwigException {
        TestClass foo = new TestClass();
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        Assert.assertEquals("Method return vale should be rendered", "foo", environment.render("foo.twig"));
    }

    private class TestClass {
        public String getSomething() {
            return "foo";
        }
    }
}
