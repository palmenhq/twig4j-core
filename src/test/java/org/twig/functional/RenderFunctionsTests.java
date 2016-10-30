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
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ foo.getSomething() }}");
        setupEnvironment(templates);

        HashMap<String, Object> ctx = new HashMap<>();
        ctx.put("foo", new TestClass());

        Assert.assertEquals("Method return vale should be rendered", "foo", environment.render("foo.twig", ctx));
    }

    public class TestClass {
        public String getSomething() {
            return "foo";
        }
    }
}
