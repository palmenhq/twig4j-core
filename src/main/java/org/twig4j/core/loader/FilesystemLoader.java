package org.twig4j.core.loader;

import org.twig4j.core.exception.LoaderException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class FilesystemLoader implements Loader {
    private Map<String, List<String>> paths;
    private Map<String, String> cache = new HashMap<>();
    private Map<String, String> errorCache = new HashMap<>();
    private final String MAIN_NAMESPACE = "__main__";

    public FilesystemLoader() {
        paths = new HashMap<>();
        paths.put(MAIN_NAMESPACE, new ArrayList<>());
    }

    public FilesystemLoader(Map<String, List<String>> paths) {
        this.paths = paths;
    }

    @Override
    public String getSource(String name) throws LoaderException {
        try {
            return new String(Files.readAllBytes(Paths.get(findTemplate(name))));
        } catch (IOException e) {
            LoaderException loaderException = new LoaderException(String.format("Error reading contents of file \"%s\".", name));
            loaderException.initCause(e);

            throw loaderException;
        }
    }

    @Override
    public String getCacheKey(String name) throws LoaderException {
        return getSource(name);
    }

    /**
     * @see #findTemplate(String, boolean) - Defaults throwExceptionWhenNotFound to true
     */
    private String findTemplate(String name) throws LoaderException {
        return findTemplate(name, true);
    }

    /**
     * Get the absolute path to a template
     *
     * @param name The template name to find
     * @param throwExceptionWhenNotFound Whether to throw an exception if the template is not found
     *
     * @return The template source
     *
     * @throws LoaderException If throwExceptionWhenNotFound is true and the template is not found
     */
    private String findTemplate(String name, boolean throwExceptionWhenNotFound) throws LoaderException {
        name = normalizeName(name);

        if (cache.containsKey(name)) {
            return cache.get(name);
        }

        if (errorCache.containsKey(name)) {
            if (throwExceptionWhenNotFound) {
                throw new LoaderException(errorCache.get(name));
            } else {
                return null;
            }
        }

        validateName(name);

        String namespace = getNamespaceFromTemplateName(name);
        String shortName = getShortNameFromTemplateName(name);

        if (!paths.containsKey(namespace)) {
            errorCache.put(name, String.format("There are no registered paths for namespace \"%s\".", namespace));

            if (throwExceptionWhenNotFound) {
                throw new LoaderException(errorCache.get(name));
            } else {
                return null;
            }
        }

        for (String path : paths.get(namespace)) {
            if (Files.isRegularFile(Paths.get(path+"/"+shortName))) {

                cache.put(name, Paths.get(path+"/"+shortName).toAbsolutePath().toString());
                System.out.println(cache.get(name));

                return cache.get(name);
            }
        }

        errorCache.put(
                name,
                String.format("Unable to find template \"%s\" (looked into: %s).", name, String.join(", ", paths.get(namespace)))
        );

        if (throwExceptionWhenNotFound) {
            throw new LoaderException(errorCache.get(name));
        } else {
            return null;
        }
    }

    /**
     * Fix multiple slashes or backslashes as DS'es
     *
     * @param name The template name
     *
     * @return The normalized template name
     */
    private String normalizeName(String name) {
        return name.replace("\\", "/").replace("/{2,}", "/");
    }

    /**
     * Prevents null byte attacks and that we're not accessing templates outside of the configured dir
     *
     * @param name The name to validate
     */
    private void validateName(String name) throws LoaderException {
        // Prevent null byte injections
        if (name.indexOf('\u0000') >= 0) {
            throw LoaderException.nullByteName();
        }

        // Prevent access outside configured dirs
        name = name.replace("^/", "");
        String[] parts = name.split("/");
        Integer level = 0;
        for (String part : parts) {
            if (part.equals("..")) {
                level--;
            } else if (!part.equals(".")) {
                level ++;
            }

            if (level < 0) {
                throw LoaderException.outsideConfiguredDirectories(name);
            }
        }
    }

    /**
     * Gets the namespace (or default if none is set)
     *
     * @param name The template name
     *
     * @return The namespace name
     */
    private String getNamespaceFromTemplateName(String name) throws LoaderException {
        if (name.matches("^@.*")) {
            if (!name.contains("/")) {
                throw LoaderException.malformedNamespacedTemplateName(name);
            }

            Pattern namspacePattern = Pattern.compile("^@([^\\/]+)\\/.*");
            Matcher matcher = namspacePattern.matcher(name);
            matcher.matches();

            return matcher.group(1);
        }

        return MAIN_NAMESPACE;
    }

    /**
     * Get a template name without namespace
     *
     * @param name The full template name
     *
     * @return The shortname
     */
    private String getShortNameFromTemplateName(String name) throws LoaderException {
        if (name.matches("^@.*")) {
            if (!name.contains("/")) {
                throw LoaderException.malformedNamespacedTemplateName(name);
            }

            Pattern pattern = Pattern.compile("^@[^/]+/(.*)");
            Matcher matcher = pattern.matcher(name);
            matcher.matches();

            return matcher.group(1);
        }

        return name.replace("^/", "");
    }

    /**
     * Add a directory path to the main namespace
     *
     * @param path The directory path to add
     *
     * @return this
     *
     * @throws LoaderException If the path doesn't exist
     */
    public FilesystemLoader addPath(String path) throws LoaderException {
        addPath(path, MAIN_NAMESPACE);

        return this;
    }

    /**
     * Add a path to a custom namespace. Will create the namespace if it doesn't exist
     *
     * @param path The directory path to add
     * @param namespace The namespace to add the directory path to
     *
     * @return this
     *
     * @throws LoaderException If the directory path is not an existing directory
     */
    public FilesystemLoader addPath(String path, String namespace) throws LoaderException {
        if (!Files.isDirectory(Paths.get(path))) {
            throw LoaderException.directoryDoesNotExist(path);
        }

        if (!paths.containsKey(namespace)) {
            paths.put(namespace, new ArrayList<>());
        }

        paths.get(namespace).add(path.replaceAll("[/|\\\\]+$", ""));

        return this;
    }

    /**
     * Add a directory path to the top of the main namespace
     *
     * @param path The directory path to add
     *
     * @return this
     *
     * @throws LoaderException If the path doesn't exist
     */
    public FilesystemLoader prependPath(String path) throws LoaderException {
        prependPath(path, MAIN_NAMESPACE);

        return this;
    }

    /**
     * Add a path to the top of a custom namespace. Will create the namespace if it doesn't exist
     *
     * @param path The directory path to add
     * @param namespace The namespace to add the directory path to
     *
     * @return this
     *
     * @throws LoaderException If the directory path is not an existing directory
     */
    public FilesystemLoader prependPath(String path, String namespace) throws LoaderException {
        if (!Files.isDirectory(Paths.get(path))) {
            throw LoaderException.directoryDoesNotExist(path);
        }

        if (!paths.containsKey(namespace)) {
            paths.put(namespace, new ArrayList<>());
        }

        paths.get(namespace).add(0, path.replaceAll("[/|\\\\]+$", ""));

        return this;
    }

    /**
     * Get all directory paths within the main namespace
     *
     * @return The directory paths
     */
    public List<String> getPaths() {
        return paths.get(MAIN_NAMESPACE);
    }

    /**
     * Get all directory paths within a namespace
     *
     * @param namespace The namespace to get directory paths from
     *
     * @return The paths
     */
    public List<String> getPaths(String namespace) {
        return paths.get(namespace);
    }

    /**
     * Set the whole path map (including namespaces)
     *
     * @param paths The path map
     *
     * @return this
     */
    public FilesystemLoader setPaths(Map<String, List<String>> paths) {
        this.paths = paths;

        return this;
    }

    /**
     * @return All namespaces
     */
    public Set<String> getNamespaces() {
        return paths.keySet();
    }
}
