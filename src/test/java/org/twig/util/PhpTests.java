package org.twig.util;

import org.junit.Assert;
import org.junit.Test;

public class PhpTests {
    @Test
    public void canDoPregMatch() {
        Assert.assertEquals("preg_match with matching regex should return \"1\"", "1", Php.preg_match("/^[a-z]/", "foo"));

        Assert.assertEquals("Can do preg_match with quotes", "1", Php.preg_match("/'\"/", "'\""));
    }
}
