package org.twigjava.loader;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.LoaderException;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ChainLoaderTests {
    @Test
    public void canLoadFromFirstLoader() throws LoaderException {
        Map<String, String> loader1Templates = new HashMap<>();
        loader1Templates.put("foo", "bar");
        Map<String, String> loader2Templates = new HashMap<>();
        loader2Templates.put("foo", "baz");
        HashMapLoader loader1 = new HashMapLoader(loader1Templates);
        HashMapLoader loader2 = new HashMapLoader(loader2Templates);

        Loader chainLoader = new ChainLoader(Arrays.asList(loader1, loader2));
        Assert.assertEquals("Should load template contents of loader 1", "bar", chainLoader.getSource("foo"));
    }

    @Test
    public void canLoadFromSecondLoader() throws LoaderException {
        Map<String, String> loader1Templates = new HashMap<>();
        loader1Templates.put("foo", "bar");
        Map<String, String> loader2Templates = new HashMap<>();
        loader2Templates.put("baz", "qux");
        HashMapLoader loader1 = new HashMapLoader(loader1Templates);
        HashMapLoader loader2 = new HashMapLoader(loader2Templates);

        Loader chainLoader = new ChainLoader(Arrays.asList(loader1, loader2));
        Assert.assertEquals("Should load template contents of loader 1", "qux", chainLoader.getSource("baz"));
    }

    @Test(expected = LoaderException.class)
    public void throwsExceptionOnMissingTemplate() throws LoaderException {
        Map<String, String> loader1Templates = new HashMap<>();
        loader1Templates.put("foo", "bar");
        Map<String, String> loader2Templates = new HashMap<>();
        loader2Templates.put("baz", "qux");
        HashMapLoader loader1 = new HashMapLoader(loader1Templates);
        HashMapLoader loader2 = new HashMapLoader(loader2Templates);

        Loader chainLoader = new ChainLoader(Arrays.asList(loader1, loader2));
        chainLoader.getSource("not a template");
    }
}
