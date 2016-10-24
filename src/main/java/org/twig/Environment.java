package org.twig;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.RuntimeTemplateCompiler;
import org.twig.exception.LoaderException;
import org.twig.loader.Loader;
import org.twig.syntax.Lexer;
import org.twig.syntax.parser.Parser;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Environment {
    private Loader loader;
    private String templatePackage = "org.twig.template";
    private String templateBaseClass = templatePackage + ".Template";
    private Lexer lexer = new Lexer();
    private Parser parser = new Parser();
    private ClassCompiler classCompiler = new ClassCompiler(this);
    private RuntimeTemplateCompiler runtimeTemplateCompiler = new RuntimeTemplateCompiler();

    public Environment() {
    }

    public Environment(Loader loader) {
        this.loader = loader;
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
    public void setTemplateBaseClass(String templateBaseClass) {
        this.templateBaseClass = templateBaseClass;
    }

    /**
     * Set the template's package name (namespace)
     * @param templatePackage The package
     */
    public void setTemplatePackage(String templatePackage) {
        this.templatePackage = templatePackage;
    }

    /**
     * Set the lexer
     * @param lexer The lexer
     */
    public void setLexer(Lexer lexer) {
        this.lexer = lexer;
    }

    /**
     * Set the parser
     * @param parser The parser
     */
    public void setParser(Parser parser) {
        this.parser = parser;
    }

    /**
     * Set the class compiler
     * @param classCompiler The class compiler
     */
    public void setClassCompiler(ClassCompiler classCompiler) {
        this.classCompiler = classCompiler;
    }

    /**
     * Set the runtime template compiler
     * @param runtimeTemplateCompiler
     */
    public void setRuntimeTemplateCompiler(RuntimeTemplateCompiler runtimeTemplateCompiler) {
        this.runtimeTemplateCompiler = runtimeTemplateCompiler;
    }
}
