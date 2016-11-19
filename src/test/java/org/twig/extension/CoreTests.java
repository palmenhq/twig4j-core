package org.twig.extension;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;

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
}
