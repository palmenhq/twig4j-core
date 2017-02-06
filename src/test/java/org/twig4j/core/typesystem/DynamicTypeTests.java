package org.twig4j.core.typesystem;

import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void canCovertToBool() {
        Assert.assertFalse("Empty string should be false", (new DynamicType("").toBoolean()));
        Assert.assertTrue("Non-empty string should be true", (new DynamicType("some string").toBoolean()));
        Assert.assertFalse("String \"0\" should be false", (new DynamicType("0").toBoolean()));

        Assert.assertFalse("Null should be false", (new DynamicType(null)).toBoolean());

        Assert.assertFalse("false should be false", (new DynamicType(false)).toBoolean());
        Assert.assertTrue("true should be true", (new DynamicType(true)).toBoolean());

        Assert.assertFalse("0 should  be false", (new DynamicType(0)).toBoolean());
        Assert.assertTrue("1 should be true", (new DynamicType(1)).toBoolean());
        Assert.assertTrue("1.0d should be true", (new DynamicType(1.0d)).toBoolean());
        Assert.assertTrue("2 should be true", (new DynamicType(2)).toBoolean());
        Assert.assertTrue("-1 should be true", (new DynamicType(-1)).toBoolean());

        Assert.assertFalse("Empty list should be false", (new DynamicType(new ArrayList<Object>())).toBoolean());
        Assert.assertTrue("List with contents should be true", (new DynamicType(Arrays.asList(new Object()))).toBoolean());

        Assert.assertFalse("Empty map should be false", (new DynamicType(new HashMap<String, Object>())).toBoolean());
        Map<String, Object> map = new HashMap<>();
        map.put("foo", new Object());
        Assert.assertTrue("Map with contents should be true", (new DynamicType(map)).toBoolean());
    }
}
