package org.twigjava.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PhpTests {
    @Test
    public void canDoPregMatch() {
        Assert.assertEquals("preg_match with matching regex should return \"1\"", "1", Php.preg_match("/^[a-z]/", "foo"));

        Assert.assertEquals("Can do preg_match with quotes", "1", Php.preg_match("/'\"/", "'\""));
    }

    @Test
    public void canGetNumberRange() {
        List<Integer> range = Php.range(1, 3);

        Assert.assertEquals("Array should be correct length", 3, range.size());

        Assert.assertEquals("First item should be correct", 1, (int)range.get(0));
        Assert.assertEquals("Second item should be correct", 2, (int)range.get(1));
        Assert.assertEquals("Third item should be correct", 3, (int)range.get(2));
    }

    @Test
    public void canGetReversedNumberRange() {
        List<Integer> range = Php.range(3, 1);

        Assert.assertEquals("Array should be correct length", 3, range.size());

        Assert.assertEquals("First item should be correct", 3, (int)range.get(0));
        Assert.assertEquals("Second item should be correct", 2, (int)range.get(1));
        Assert.assertEquals("Third item should be correct", 1, (int)range.get(2));
    }

    @Test
    public void canGetNumberWithInterval() {
        List<Integer> range = Php.range(0, 30, 10);

        Assert.assertEquals("Array should be correct length", 4, range.size());

        Assert.assertEquals("First item should be correct", 0, (int)range.get(0));
        Assert.assertEquals("Second item should be correct", 10, (int)range.get(1));
        Assert.assertEquals("Third item should be correct", 20, (int)range.get(2));
        Assert.assertEquals("Fourth item should be correct", 30, (int)range.get(3));
    }

    @Test
    public void canDoAlphabeticRange() {
        List<String> range = Php.range("a", "d");

        Assert.assertEquals("Array should be correct length", 4, range.size());

        Assert.assertEquals("First item should be correct", "a", range.get(0));
        Assert.assertEquals("Second item should be correct", "b", range.get(1));
        Assert.assertEquals("Third item should be correct", "c", range.get(2));
        Assert.assertEquals("Fourth item should be correct", "d", range.get(3));
    }

    @Test
    public void canDoReverseAlphabeticRange() {
        List<String> range = Php.range("d", "a");

        Assert.assertEquals("Array should be correct length", 4, range.size());

        Assert.assertEquals("First item should be correct", "d", range.get(0));
        Assert.assertEquals("Second item should be correct", "c", range.get(1));
        Assert.assertEquals("Third item should be correct", "b", range.get(2));
        Assert.assertEquals("Fourth item should be correct", "a", range.get(3));
    }
}
