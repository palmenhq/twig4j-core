package org.twig4j.core.loader;

import org.twig4j.core.exception.LoaderException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Scanner;

public class ResourceLoader implements Loader {
    ClassLoader classLoader;

    public ResourceLoader(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @Override
    public String getSource(String name) throws LoaderException {
        InputStream stream = classLoader.getResourceAsStream(name);

        if (stream == null) {
            throw LoaderException.notDefined(name);
        }

        Scanner scanner = new Scanner(stream).useDelimiter("\\A");

        if (scanner.hasNext()) {
            String templateContents = scanner.next();
            try {
                stream.close();
            } catch (IOException e) {}

            return templateContents;
        }

        throw LoaderException.notDefined(name);
    }

    @Override
    public String getCacheKey(String name) throws LoaderException {
        return getSource(name);
    }
}
