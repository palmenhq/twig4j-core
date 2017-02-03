package org.twigjava.loader;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.LoaderException;

import java.io.ByteArrayInputStream;

import static org.mockito.Mockito.*;

public class ResourceLoaderTests {
    @Test
    public void canGetResourceContents() throws LoaderException {
        ClassLoader classLoader = mock(ClassLoader.class);
        byte[] bytes = new byte[3];
        bytes[0] = 'f';
        bytes[1] = 'o';
        bytes[2] = 'o';

        when(classLoader.getResourceAsStream(eq("test"))).thenReturn(new ByteArrayInputStream(bytes));

        ResourceLoader resourceLoader = new ResourceLoader(classLoader);

        Assert.assertEquals("getSource should return \"file contents\"", "foo", resourceLoader.getSource("test"));
    }

    @Test
    public void canGetResourceCacheKey() throws LoaderException {
        ClassLoader classLoader = mock(ClassLoader.class);
        byte[] bytes = new byte[3];
        bytes[0] = 'f';
        bytes[1] = 'o';
        bytes[2] = 'o';

        when(classLoader.getResourceAsStream(eq("test"))).thenReturn(new ByteArrayInputStream(bytes));

        ResourceLoader resourceLoader = new ResourceLoader(classLoader);

        Assert.assertEquals("getCacheKey should return \"file contents\"", "foo", resourceLoader.getCacheKey("test"));
    }

    @Test
    public void canLoadRealTemplate() throws LoaderException {
        ResourceLoader resourceLoader = new ResourceLoader(getClass().getClassLoader());

        Assert.assertEquals("Should return template contents", "template A\n", resourceLoader.getSource("test-templates/templateA.twig"));
    }

    @Test(expected = LoaderException.class)
    public void cantLoadNonExistingTemplate() throws LoaderException {
        ResourceLoader resourceLoader = new ResourceLoader(getClass().getClassLoader());

        Assert.assertEquals("Should return template contents", "template A\n", resourceLoader.getSource("nonExistingTemplate"));
    }
}
