package org.twigjava.extension;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

public class CoreTests {
    @Test
    public void canEnsureIterableOnIterable() {
        Iterable<String> list = new ArrayList<>();
        Iterable<String> ensuredList = (Iterable<String>)Core.ensureIterable(list);

        Assert.assertSame("Returned list should be the same as the provided list", list, ensuredList);
    }

    @Test
    public void canEnsureIterableOnMap() {
        Map<String, String> map = new HashMap<>();
        map.put("foo", "bar");
        Iterable<Map.Entry<String, String>> ensuredList = (Iterable<Map.Entry<String, String>>)Core.ensureIterable(map);

        for (Map.Entry<String, String> thing : ensuredList) {
            Assert.assertEquals("Returned entry set key should be correct", "foo", thing.getKey());
            Assert.assertEquals("Returned entry set value should be correct", "bar", thing.getValue());
        }
    }

    @Test
    public void canEnsureIterableOnArray() {
        Integer[] integers = new Integer[] {1};
        Iterable<Integer> ensuredList = (Iterable<Integer>)Core.ensureIterable(integers);

        for (Integer thing : ensuredList) {
            Assert.assertEquals("Returned integer should be correct", (Integer)1, thing);
        }
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
