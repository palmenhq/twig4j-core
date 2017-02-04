package org.twig4j.core.exception;

import org.junit.Assert;
import org.junit.Test;

public class TwigExceptionTests {
    @Test
    public void canCreateWithPlainMessage() {
        Twig4jException e = new Twig4jException("error");

        Assert.assertEquals(e.getMessage(), "error");
    }

    @Test
    public void canCreateWithTemplateNameAndLineNumberAndTrailingDot() {
        Twig4jException e = new Twig4jException("Error.", "foo.html.twig4j", 1337);

        Assert.assertEquals(e.getMessage(), "Error in \"foo.html.twig4j\" at line 1337.");
    }

    @Test
    public void canCreateWithTemplateNameAndLineNumberAndTrailingQuestionMark() {
        Twig4jException e = new Twig4jException("Error?", "foo.html.twig4j", 1337);

        Assert.assertEquals(e.getMessage(), "Error in \"foo.html.twig4j\" at line 1337?");
    }
}
