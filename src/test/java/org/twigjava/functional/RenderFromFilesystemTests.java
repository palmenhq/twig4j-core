package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.exception.TwigException;
import org.twigjava.loader.FilesystemLoader;
import org.twigjava.template.Context;

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
