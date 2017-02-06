package org.twig4j.core.typesystem;

import org.junit.Assert;
import org.junit.Test;

public class DynamicTypeTests {
    @Test
    public void canCompareDecimalsToInts() {
        Assert.assertEquals("1.2 should be greater than 1", 1, (new DynamicType(1.2).compareTo(new DynamicType(1))));
        Assert.assertEquals("1.2 should be greater than 1", 1, (new DynamicType(1.2f).compareTo(new DynamicType(1))));
        Assert.assertEquals("1.2 should be greater than 1", 1, (new DynamicType(1.2d).compareTo(new DynamicType(1))));

        Assert.assertEquals("0.2 should be less than 1", -1, (new DynamicType(.2).compareTo(new DynamicType(1))));
        Assert.assertEquals("0.2 should be less than 1", -1, (new DynamicType(.2f).compareTo(new DynamicType(1))));
        Assert.assertEquals("0.2 should be less than 1", -1, (new DynamicType(.2d).compareTo(new DynamicType(1))));

        Assert.assertEquals("1.0 should be equal to 1", 0, (new DynamicType(1.0).compareTo(new DynamicType(1))));
        Assert.assertEquals("1.0 should be equal to 1", 0, (new DynamicType(1.0f).compareTo(new DynamicType(1))));
        Assert.assertEquals("1.0 should be equal to 1", 0, (new DynamicType(1.0d).compareTo(new DynamicType(1))));
    }

    @Test
    public void canCalculateBetweenTypes() {
        Assert.assertEquals("1.0 + 1 should be 2", 2, (new DynamicType(1.0)).add(new DynamicType(1)));
        Assert.assertEquals("1.1 + 1 should be 2.1", 2.1, (new DynamicType(1.1)).add(new DynamicType(1)));

        Assert.assertEquals("2.0 - 1 should be 1", 1, (new DynamicType(2.0)).subtract(new DynamicType(1)));
        Assert.assertEquals("2.1 - 1 should be 1.1", 1.1, (new DynamicType(2.1)).subtract(new DynamicType(1)));

        Assert.assertEquals("2.0 * 2 should be 4", 4, (new DynamicType(2.0)).multiply(new DynamicType(2)));
        Assert.assertEquals("2.1 * 2 should be 4.2", 4.2, (new DynamicType(2.1)).multiply(new DynamicType(2)));

        Assert.assertEquals("2.0 / 2 should be 1", 1, (new DynamicType(2.0)).divide(new DynamicType(2)));
        Assert.assertEquals("2.2 / 2 should be 1.1", 1.1, (new DynamicType(2.2)).divide(new DynamicType(2)));

        Assert.assertEquals("2.5 // 2 should be 1", 1, (new DynamicType(2.5)).floorDivide(new DynamicType(2)));

        Assert.assertEquals("3.0 % 2 should be 1", 1, (new DynamicType(3.0)).mod(new DynamicType(2)));
        Assert.assertEquals("2.2 % 2 should be 0.2", 0.2, (new DynamicType(2.2)).mod(new DynamicType(2)));

        Assert.assertEquals("2.0 ** 2 should be 4", 4, (new DynamicType(2.0)).pow(new DynamicType(2)));
        Assert.assertEquals("2.2 ** 2 should be 4.84", 4.84, (new DynamicType(2.2)).pow(new DynamicType(2)));
    }

    @Test
    public void canEqualDifferentTypes() {
        Assert.assertTrue("String of value 2 should equal int of value 2", (new DynamicType("2")).equals(new DynamicType(2)));
        Assert.assertTrue("String of value 2 should equal float of value 2", (new DynamicType("2")).equals(new DynamicType(2f)));
    }
}
