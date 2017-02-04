package org.twig4j.core.loader;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.LoaderException;

public class FilesystemLoaderTests {
    @Test
    public void canAddExistingDirectory() throws LoaderException {
        String testTemplatesDirectoryPath = getClass().getClassLoader().getResource("test-templates").getPath();
        FilesystemLoader loader = new FilesystemLoader();
        loader.addPath(testTemplatesDirectoryPath);

        Assert.assertTrue("Paths should contain the added path", loader.getPaths().contains(testTemplatesDirectoryPath));
    }

    @Test
    public void canAddExistingDirectoryWithTrailingSlash() throws LoaderException {
        String testTemplatesDirectoryPath = getClass().getClassLoader()
                .getResource("test-templates")
                .getPath();
        FilesystemLoader loader = new FilesystemLoader();
        loader.addPath(testTemplatesDirectoryPath+"/");

        Assert.assertTrue("Paths should contain the added path without trailing slash", loader.getPaths().contains(testTemplatesDirectoryPath));
    }

    @Test(expected = LoaderException.class)
    public void cantAddNonExistingDirectory() throws LoaderException {
        FilesystemLoader loader = new FilesystemLoader();

        loader.addPath("nonExistingDirectory");
    }

    @Test
    public void canGetCacheKey() throws LoaderException {
        FilesystemLoader loader = new FilesystemLoader();
        String templatesDirectory = getClass().getClassLoader().getResource("test-templates").getPath();
        loader.addPath(templatesDirectory);

        Assert.assertEquals("Cache key should be absolute path to template","template A\n", loader.getCacheKey("templateA.twig"));
    }

    @Test
    public void canLoadTemplateWithRegularName() throws LoaderException {
        FilesystemLoader loader = new FilesystemLoader();
        String templatesDirectory = getClass().getClassLoader().getResource("test-templates").getPath();
        loader.addPath(templatesDirectory);

        Assert.assertEquals("Template contents should be returned", "template A\n", loader.getSource("templateA.twig"));
    }

    @Test(expected = LoaderException.class)
    public void cantLoadTemplateOutsideConfiguredDir() throws LoaderException {
        FilesystemLoader loader = new FilesystemLoader();
        String templatesDirectory = getClass().getClassLoader().getResource("test-templates").getPath();
        loader.addPath(templatesDirectory);

        loader.getSource("../foo.twig");
    }

    @Test
    public void canLoadTemplateInSubDirectory() throws LoaderException {
        FilesystemLoader loader = new FilesystemLoader();
        String templatesDirectory = getClass().getClassLoader().getResource("test-templates").getPath();
        loader.addPath(templatesDirectory);

        Assert.assertEquals("Template contents should be returned", "template B\n", loader.getSource("subdir/templateB.twig"));
    }

    @Test
    public void canLoadTemplateWithNamespace() throws LoaderException {
        FilesystemLoader loader = new FilesystemLoader();
        String templatesDirectory = getClass().getClassLoader().getResource("test-templates").getPath();
        loader.addPath(templatesDirectory, "fooNamespace");

        Assert.assertEquals("Template contents should be returned", "template B\n", loader.getSource("@fooNamespace/subdir/templateB.twig"));
    }
}
