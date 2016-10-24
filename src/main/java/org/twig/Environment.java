package org.twig;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.RuntimeTemplateCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigException;
import org.twig.loader.Loader;
import org.twig.syntax.Lexer;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.Module;
import org.twig.template.Template;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;

public class Environment {
    private Loader loader;
    private String templatePackage = "org.twig.template";
    private String templateBaseClass = "org.twig.template.Template";
    private Lexer lexer = new Lexer();
    private Parser parser = new Parser();
    private ClassCompiler classCompiler = new ClassCompiler(this);
    private RuntimeTemplateCompiler runtimeTemplateCompiler = new RuntimeTemplateCompiler();
    private HashMap<String, Template> loadedTemplates = new HashMap<>();

    public Environment() {
    }

    public Environment(Loader loader) {
        this.loader = loader;
    }

    public Template loadTemplate(String name) throws LoaderException, TwigException {
        return loadTemplate(name, 0);
    }

    public String render(String name) throws LoaderException, TwigException {
        return loadTemplate(name).render();
    }

    public String render(String name, HashMap<String, String> context) throws LoaderException, TwigException {
        return loadTemplate(name).render(context);
    }

    public Template loadTemplate(String name, Integer index) throws LoaderException, TwigException {
        String className = getTemplateClass(name);
        String fullTemplateClassName = templatePackage + "." + className;

        // Return already compiled template
        if (loadedTemplates.containsKey(className)) {
            return loadedTemplates.get(className);
        }

        try {
            Template template = (Template) Class.forName(fullTemplateClassName).newInstance();

            return template;
        } catch (ClassNotFoundException e) {
            String javaSourceCode = compileSource(getLoader().getSource(name), name);
            Template template = this.runtimeTemplateCompiler.compile(javaSourceCode, fullTemplateClassName);

            this.loadedTemplates.put(className, template);

            return template;
        } catch (Exception e) {
            throw new TwigException(e.getMessage(), name, -1, e);
        }
    }

    /**
     * Compile a template source code into java code
     * @param templateSourceCode the source code to compile
     * @param name the name of the template file
     * @return Compiled java code
     * @throws TwigException on syntax or loader errors
     */
    public String compileSource(String templateSourceCode, String name) throws TwigException {
        try {
            TokenStream tokenStream = lexer.tokenize(templateSourceCode, name);
            Module module = parser.parse(tokenStream);

            String compiledClassSourceCode = classCompiler.compile(module).getSourceCode();

            return compiledClassSourceCode;
        } catch (TwigException e) {
            e.setTemplateName(name);

            throw e;
        }
    }

    /**
     * @see this#getTemplateClass For docs
     */
    public String getTemplateClass(String name) throws LoaderException {
        return this.getTemplateClass(name, 0);
    }

    /**
     * Gets the template class associated with the given string.
     *
     * The generated template class is based on the following parameters:
     *
     *  * The cache key for the given template
     *
     * @param name The template name to get the class name from
     * @param index Idk what this is yet
     * @return The class name
     * @throws LoaderException If the template does not exist
     */
    public String getTemplateClass(String name, Integer index) throws LoaderException {
        String key = this.getLoader().getCacheKey(name);

        return hashText(key).concat("_").concat(index.toString());
    }

    /**
     * Create a sha 256 hash from something
     * @param text The text to hash
     * @return The checksum
     */
    private String hashText(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] digest = md.digest(text.getBytes("UTF-8"));

            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            // This'll never happen
            throw new RuntimeException("Something impossible just happened");
        } catch (NoSuchAlgorithmException e) {
            // This'll never happen
            throw new RuntimeException("Something impossible just happened");
        }
    }

    /**
     * Get the template loader
     * @return
     */
    public Loader getLoader() {
        return loader;
    }

    /**
     * Set the template loader
     * @param loader
     */
    public Environment setLoader(Loader loader) {
        this.loader = loader;

        return this;
    }

    /**
     * Get the template base class name
     * @return The template base class
     */
    public String getTemplateBaseClass() {
        return templateBaseClass;
    }

    /**
     * Set the template base class name
     * @param templateBaseClass The template base class
     */
    public Environment setTemplateBaseClass(String templateBaseClass) {
        this.templateBaseClass = templateBaseClass;

        return this;
    }

    /**
     * Get the template package
     * @return The template package
     */
    public String getTemplatePackage() {
        return templatePackage;
    }

    /**
     * Set the template's package name (namespace)
     * @param templatePackage The package
     */
    public Environment setTemplatePackage(String templatePackage) {
        this.templatePackage = templatePackage;

        return this;
    }

    /**
     * Set the lexer
     * @param lexer The lexer
     */
    public Environment setLexer(Lexer lexer) {
        this.lexer = lexer;

        return this;
    }

    /**
     * Set the parser
     * @param parser The parser
     */
    public Environment setParser(Parser parser) {
        this.parser = parser;

        return this;
    }

    /**
     * Set the class compiler
     * @param classCompiler The class compiler
     */
    public Environment setClassCompiler(ClassCompiler classCompiler) {
        this.classCompiler = classCompiler;

        return this;
    }

    /**
     * Set the runtime template compiler
     * @param runtimeTemplateCompiler
     */
    public Environment setRuntimeTemplateCompiler(RuntimeTemplateCompiler runtimeTemplateCompiler) {
        this.runtimeTemplateCompiler = runtimeTemplateCompiler;

        return this;
    }

    /**
     * Get loaded templates
     * @return
     */
    public HashMap<String, Template> getLoadedTemplates() {
        return loadedTemplates;
    }

    /**
     * Set all loaded templates
     * @param loadedTemplates
     * @return
     */
    public Environment setLoadedTemplates(HashMap<String, Template> loadedTemplates) {
        this.loadedTemplates = loadedTemplates;

        return this;
    }
}
