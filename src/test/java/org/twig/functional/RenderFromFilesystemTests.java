package org.twig.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.loader.FilesystemLoader;

import java.util.HashMap;

public class RenderFromFilesystemTests {
    @Test
    public void canLoadAndRenderTemplateFromFilesystem() throws TwigException {
        FilesystemLoader loader = new FilesystemLoader();
        loader.addPath(getClass().getClassLoader().getResource("test-templates").getPath().toString());
        Environment environment = new Environment(loader);

        HashMap<String, WhatClass> ctx = new HashMap<>();
        ctx.put("what", new WhatClass());

        Assert.assertEquals(
                "Loaded template should be rendered correctly",
                "Hello World!\n",
                environment.render("subdir/functional-test-template.twig", ctx)
        );
    }

    public class WhatClass {
        public String place() {
            return "World";
        }
    }
}
