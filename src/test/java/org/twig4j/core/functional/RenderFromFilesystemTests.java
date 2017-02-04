package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.loader.FilesystemLoader;
import org.twig4j.core.template.Context;

public class RenderFromFilesystemTests {
    @Test
    public void canLoadAndRenderTemplateFromFilesystem() throws TwigException {
        FilesystemLoader loader = new FilesystemLoader();
        loader.addPath(getClass().getClassLoader().getResource("test-templates").getPath().toString());
        Environment environment = new Environment(loader);

        Context ctx = new Context();
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
