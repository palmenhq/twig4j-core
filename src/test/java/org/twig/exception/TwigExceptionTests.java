package org.twig.exception;

import org.junit.Assert;
import org.junit.Test;

public class TwigExceptionTests {
    @Test
    public void canCreateWithPlainMessage() {
        TwigException e = new TwigException("error");

        Assert.assertEquals(e.getMessage(), "error");
    }

    @Test
    public void canCreateWithTemplateNameAndLineNumberAndTrailingDot() {
        TwigException e = new TwigException("Error.", 1337, "foo.html.twig");

        Assert.assertEquals(e.getMessage(), "Error in foo.html.twig at line 1337.");
    }

    @Test
    public void canCreateWithTemplateNameAndLineNumberAndTrailingQuestionMark() {
        TwigException e = new TwigException("Error?", 1337, "foo.html.twig");

        Assert.assertEquals(e.getMessage(), "Error in foo.html.twig at line 1337?");
    }
}
