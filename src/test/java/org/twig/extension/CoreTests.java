package org.twig.extension;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CoreTests {
    @Test
    public void canEnsureIterableOnIterable() {
        Iterable<String> list = new ArrayList<>();
        Iterable<String> ensuredList = (Iterable<String>)Core.ensureIterable(list);

        Assert.assertSame("Returned list should be the same as the provided list", list, ensuredList);
    }

    @Test
    public void canEnsureIterableOnNonIterable() {
        String notAList = null;
        Iterable<String> ensuredList = (Iterable<String>)Core.ensureIterable(notAList);

        Assert.assertTrue("Returned list should be a list even though null and another type was provided", ensuredList instanceof Iterable);
    }

    // TODO test ensureIterable(Map<?>)

    @Test
    public void canCheckInOnString() {
        String haystack = "bar foo baz";

        Assert.assertTrue("Should find foo", Core.inFilter("foo", haystack));
        Assert.assertFalse("Should not find something that's not in the string", Core.inFilter("not in string", haystack));
    }

    @Test
    public void canCheckInOnList() {
        List<String> haystack = Arrays.asList("foo", "bar", "baz");

        Assert.assertTrue("Should find object that's present in array", Core.inFilter("foo", haystack));
        Assert.assertFalse("Should not find object that's not present in array", Core.inFilter("not in the list", haystack));
    }
}
