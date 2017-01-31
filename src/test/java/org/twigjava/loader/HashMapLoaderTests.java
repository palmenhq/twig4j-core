package org.twigjava.loader;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.LoaderException;

import java.util.HashMap;

public class HashMapLoaderTests {
    @Test
    public void canGetSource() throws LoaderException {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("foo", "bar");

        HashMapLoader loader = new HashMapLoader(hashMap);

        Assert.assertEquals("bar", loader.getSource("foo"));
    }

    @Test(expected = LoaderException.class)
    public void cantGetSourceWhenTemplateNotExists() throws LoaderException {
        HashMapLoader loader = new HashMapLoader();

        loader.getSource("foo");
    }

    @Test
    public void canGetCacheKey() throws LoaderException {
        HashMap<String, String> hashMap = new HashMap<String, String>();

        hashMap.put("foo", "bar");

        HashMapLoader loader = new HashMapLoader(hashMap);

        Assert.assertEquals("bar", loader.getCacheKey("foo"));
    }

    @Test(expected = LoaderException.class)
    public void cantGetCacheKeyWhenTemplateNotExists() throws LoaderException {
        HashMapLoader loader = new HashMapLoader();

        loader.getSource("foo");
    }
}
