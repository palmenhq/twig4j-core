package org.twig;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.LoaderException;
import org.twig.loader.HashMapLoader;
import org.twig.loader.Loader;

import java.util.HashMap;

public class EnvironmentTests {
    @Test
    public void testGetTemplateClass() throws LoaderException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo", "bar");
        Loader loader = new HashMapLoader(templates);
        Environment environment = new Environment(loader);

        // Just run this and make sure it doesn't throw any errors
        String className = environment.getTemplateClass("foo");
        // Assume template name = sha 256 of "bar"
        Assert.assertEquals(
                "Class name should be SHA256 of passed template name content + underscore 0",
                "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9_0",
                className
        );

        String classNameWithDefinedIndex = environment.getTemplateClass("foo", 1);
        Assert.assertEquals(
                "Class name should be SHA256 of passed template name content + underscore index",
                "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9_1",
                classNameWithDefinedIndex
        );
    }
}
