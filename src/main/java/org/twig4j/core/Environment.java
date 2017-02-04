package org.twig4j.core;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.compiler.RuntimeTemplateCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.extension.Core;
import org.twig4j.core.extension.Extension;
import org.twig4j.core.filter.Filter;
import org.twig4j.core.loader.Loader;
import org.twig4j.core.syntax.Lexer;
import org.twig4j.core.syntax.TokenStream;
import org.twig4j.core.syntax.operator.Operator;
import org.twig4j.core.syntax.parser.Parser;
import org.twig4j.core.syntax.parser.node.Module;
import org.twig4j.core.syntax.parser.tokenparser.AbstractTokenParser;
import org.twig4j.core.template.Context;
import org.twig4j.core.template.Template;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Environment {
    private String templatePackage = "org.twig4j.core.template";
    private String templateClassPrefix = "Template_";
    private String templateBaseClass = "org.twig4j.core.template.Template";
    private boolean strictVariables = false;
    private boolean strictTypes = true;
    private boolean debug = false;

    private LinkedHashMap<String, Operator> binaryOperators = new LinkedHashMap<>();
    private LinkedHashMap<String, Operator> unaryOperators = new LinkedHashMap<>();
    private Map<String, AbstractTokenParser> tokenParsers = new HashMap<>();
    private boolean hasInitedExtensions = false;

    private Loader loader;
    private Lexer lexer = new Lexer(this);
    private Parser parser = new Parser(this);
    private ClassCompiler classCompiler = new ClassCompiler(this);
    private RuntimeTemplateCompiler runtimeTemplateCompiler = new RuntimeTemplateCompiler(this);
    private HashMap<String, Template> loadedTemplates = new HashMap<>();

    private List<Extension> extensions = new ArrayList<>();
    private Map<String, Filter> filters = new HashMap<>();

    public Environment() {
        init();
    }

    public Environment(Loader loader) {
        this.loader = loader;

        init();
    }

    /**
     * Initializes the extensions required for twig4j
     */
    private void init() {
        addExtension(new Core());
    }

    /**
     * Defaults context to empty context.
     * @see #render(String, Context) For how rendering is done.
     *
     * @param name The name of the template to render
     *
     * @return Rendered result
     *
     * @throws TwigException If errors are encountered during any of the rendering phases
     */
    public String render(String name) throws TwigException {
        return loadTemplate(name).render();
    }

    /**
     * Loads a template and call's it's render method.
     * @see Template#render(Context) for render without Context
     * @see #loadTemplate(String) For how loading is done
     *
     * @param name The name of the template to render (defaults index to 0)
     * @param context A map of the variables to provide in the template
     *
     * @return The rendered contents as a string
     *
     * @throws TwigException If errors are encountered during any of the rendering phases
     */
    public String render(String name, Context context) throws TwigException {
        return loadTemplate(name).render(context);
    }

    /**
     * Loads a singe template
     * @see #loadTemplate(String) For how templates are loaded
     *
     * @param templateName The template name to resolve
     *
     * @return The template object
     *
     * @throws TwigException If there are any errors resolving the template
     *
     */
    public Template resolveTemplate(String templateName) throws TwigException {
        return resolveTemplate(Arrays.asList(templateName));
    }

    /**
     * Loads the first template of the list
     *
     * @param templates The list of templates to load
     *
     * @return The first resolved template in the list
     *
     * @throws TwigException If there are any errors resolving the template
     */
    public Template resolveTemplate(List<String> templates) throws TwigException {
        Integer templatesCount = templates.size();

        for (String template : templates) {
            try {
                return loadTemplate(((String) template));
            } catch (LoaderException e) {
                // Do nothing if there are multiple templates to find. If there are more templates and none of them are found throw
                // the error at the end of this method
                if (templatesCount == 1) {
                    throw e;
                }
            } catch (TwigException e) {
                // Always throw errors on other exceptions than loader exceptions;
                throw e;
            }
        }

        throw new LoaderException(
            String.format(
                "Unable to find one of the following templates: \"%s\".",
                String.join(", ", templates)
            )
        );
    }

    /**
     * Loads a template and Defaults index to 0
     * @see #loadTemplate(String, Integer)
     *
     * @param name The name of the template to load
     *
     * @return The loaded template instance
     *
     * @throws TwigException On any errors
     */
    public Template loadTemplate(String name) throws TwigException {
        return loadTemplate(name, 0);
    }

    /**
     * Resolved a template (from cache or provided loader), and returns a template object ready to render
     *
     * @param name The name of the template
     * @param index The index TODO find out what this is
     *
     * @return The loaded template instance
     *
     * @throws TwigException On any errors
     */
    public Template loadTemplate(String name, Integer index) throws TwigException {
        if (!hasInitedExtensions) {
            initExtensions();
        }

        String className = getTemplateClass(name);
        String fullTemplateClassName = templatePackage + "." + className;

        // Return already compiled template
        if (loadedTemplates.containsKey(className)) {
            return loadedTemplates.get(className);
        }

        try {
            // First try to load template with Environment constructor, if that doesn't work load it with default constructor
            try {
                Template template = (Template) Class.forName(fullTemplateClassName).getConstructor(Environment.class).newInstance(this);

                return template;
            } catch (NoSuchMethodException e) {
                Template template = (Template) Class.forName(fullTemplateClassName).newInstance();
                template.setEnvironment(this);

                return template;
            }
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
     *
     * @param templateSourceCode the source code to compile
     *
     * @param name the name of the template file
     *
     * @return Compiled java code
     *
     * @throws TwigException on syntax or loader errors
     */
    public String compileSource(String templateSourceCode, String name) throws TwigException {
        if (!hasInitedExtensions) {
            initExtensions();
        }

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
     * @see #getTemplateClass(String, Integer)
     *
     * @param name The name eof the template
     *
     * @return The template class name
     *
     * @throws LoaderException If the template doesn't exist
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
     *
     * @param index Idk what this is yet
     *
     * @return The class name
     *
     * @throws LoaderException If the template does not exist
     */
    public String getTemplateClass(String name, Integer index) throws LoaderException {
        String key = this.getLoader().getCacheKey(name);

        return getTemplateClassPrefix().concat(hashText(key)).concat("_").concat(index.toString());
    }

    /**
     * Create a sha 256 hash from something
     *
     * @param text The text to hash
     *
     * @return The checksum
     */
    private String hashText(String text) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            byte[] digest = md.digest(text.getBytes("UTF-8"));

            return DatatypeConverter.printHexBinary(digest).toLowerCase();
        } catch (UnsupportedEncodingException e) {
            // This'll never happen
            throw new RuntimeException("Something impossible just happened", e);
        } catch (NoSuchAlgorithmException e) {
            // This'll never happen
            throw new RuntimeException("Something impossible just happened", e);
        }
    }

    /**
     * Initializes all extensions added to Twig
     *
     * @return this
     *
     * @throws TwigRuntimeException If any of the extensions errors during initialization
     */
    protected Environment initExtensions() throws TwigRuntimeException {
        if (hasInitedExtensions) {
            return this;
        }

        hasInitedExtensions = true;

        for (Extension extension : extensions) {
            initExtension(extension);
        }

        return this;
    }

    /**
     * Initialize a singe extension
     *
     * @param extension The extension to init
     *
     * @throws TwigRuntimeException If errors are encountered during initialization
     */
    protected void initExtension(Extension extension) throws TwigRuntimeException {

        // Operators
        this.unaryOperators.putAll(extension.getUnaryOperators());
        this.binaryOperators.putAll(extension.getBinaryOperators());
        this.filters.putAll(extension.getFilters());

        // Token parsers
        for (AbstractTokenParser tokenParser : extension.getTokenParsers()) {
            this.tokenParsers.put(tokenParser.getTag(), tokenParser);
        }
    }

    /**
     * Get the template loader
     *
     * @return The loader
     */
    public Loader getLoader() {
        return loader;
    }

    /**
     * Set the template loader
     *
     * @param loader The loader
     *
     * @return this
     */
    public Environment setLoader(Loader loader) {
        this.loader = loader;

        return this;
    }

    /**
     * Get the template base class name
     *
     * @return The template base class
     */
    public String getTemplateBaseClass() {
        return templateBaseClass;
    }

    /**
     * Set the template base class name
     *
     * @param templateBaseClass The template base class
     *
     * @return this
     */
    public Environment setTemplateBaseClass(String templateBaseClass) {
        this.templateBaseClass = templateBaseClass;

        return this;
    }

    /**
     * Get the template class prefix (default Template_)
     *
     * @return this
     */
    public String getTemplateClassPrefix() {
        return templateClassPrefix;
    }

    /**
     * Set the template class prefix
     *
     * @param templateClassPrefix The template class prefix
     */
    public void setTemplateClassPrefix(String templateClassPrefix) {
        this.templateClassPrefix = templateClassPrefix;
    }

    /**
     * Get the template package
     *
     * @return The template package
     */
    public String getTemplatePackage() {
        return templatePackage;
    }

    /**
     * Set the template's package name (namespace)
     *
     * @param templatePackage The package
     *
     * @return this
     */
    public Environment setTemplatePackage(String templatePackage) {
        this.templatePackage = templatePackage;

        return this;
    }

    /**
     * Whether strict variables (throw exception instead of returning null) are used. Disabled by default.
     *
     * @return Whether strict variables are used.
     */
    public boolean isStrictVariables() {
        return strictVariables;
    }

    /**
     * Enable strict variables (throw exception instead of returning null)
     *
     * @return this
     */
    public Environment enableStrictVariables() {
        strictVariables = true;

        return this;
    }

    /**
     * Disable strict variables (return null instead of throwing exceptions)
     *
     * @return this
     */
    public Environment disableStrictVariables() {
        strictVariables = false;

        return this;
    }

    /**
     * Whether to throw exceptions on typing errors (i.e. comparing bool to string) or just return false silently.
     * Enabled by default.
     *
     * @return Whether to use strict types
     */
    public boolean isStrictTypes() {
        return strictTypes;
    }

    /**
     * Enable strict types (throw exceptions on typing errors)
     *
     * @return this
     */
    public Environment enableStrictTypes() {
        strictTypes = true;

        return this;
    }

    /**
     * Disable strict types (return false or null)
     *
     * @return this
     */
    public Environment disableStrictTypes() {
        strictTypes = false;

        return this;
    }

    /**
     * Whether to debug Twig. Disabled by default.
     *
     * @return Whether debug mode is on
     */
    public boolean isDebug() {
        return debug;
    }

    /**
     * Enable debugging (for development purposes)
     *
     * @return this
     */
    public Environment enableDebug() {
        debug = true;

        return this;
    }

    /**
     * Disable debugging
     *
     * @return this
     */
    public Environment disableDebug() {
        debug = false;

        return this;
    }

    /**
     * Get all registered binary operators
     *
     * @return All binary operators
     *
     * @throws TwigRuntimeException If any of the extensions fails to initialize
     */
    public LinkedHashMap<String, Operator> getBinaryOperators() throws TwigRuntimeException {
        if (!hasInitedExtensions) {
            initExtensions();
        }

        return binaryOperators;
    }

    /**
     * Puts a binary operator in the map
     *
     * @param name The name of the operator
     * @param operator The operator object
     *
     * @return this
     */
    public Environment addBinaryOperator(String name, Operator operator) {
        binaryOperators.put(name, operator);

        return this;
    }

    /**
     * Get all registered unary operators
     *
     * @return The unary operators
     *
     * @throws TwigRuntimeException If any of the extensions fails to initialize
     */
    public LinkedHashMap<String, Operator> getUnaryOperators() throws TwigRuntimeException {
        if (!hasInitedExtensions) {
            initExtensions();
        }

        return unaryOperators;
    }

    /**
     * Add a single unary operator
     *
     * @param name The name of the operator
     * @param operator The operator object
     *
     * @return this
     */
    public Environment addUnaryOperator(String name, Operator operator) {
        unaryOperators.put(name, operator);

        return this;
    }

    /**
     * Get all token parsers/handlers
     *
     * @return The token parsers
     */
    public Map<String, AbstractTokenParser> getTokenParsers() {
        return tokenParsers;
    }

    /**
     * Get the environment's lexer
     *
     * @return The lexer
     */
    public Lexer getLexer() {
        return lexer;
    }

    /**
     * Set the lexer
     *
     * @param lexer The lexer
     *
     * @return this
     */
    public Environment setLexer(Lexer lexer) {
        this.lexer = lexer;

        return this;
    }

    /**
     * Set the parser
     *
     * @param parser The parser
     *
     * @return this
     */
    public Environment setParser(Parser parser) {
        this.parser = parser;

        return this;
    }

    /**
     * Set the class compiler
     *
     * @param classCompiler The class compiler
     *
     * @return this
     */
    public Environment setClassCompiler(ClassCompiler classCompiler) {
        this.classCompiler = classCompiler;

        return this;
    }

    /**
     * Set the runtime template compiler
     *
     * @param runtimeTemplateCompiler The runtime template compiler
     *
     * @return this
     */
    public Environment setRuntimeTemplateCompiler(RuntimeTemplateCompiler runtimeTemplateCompiler) {
        this.runtimeTemplateCompiler = runtimeTemplateCompiler;

        return this;
    }

    /**
     * Get loaded templates
     *
     * @return The loaded templates
     */
    public HashMap<String, Template> getLoadedTemplates() {
        return loadedTemplates;
    }

    /**
     * Set all loaded templates
     *
     * @param loadedTemplates The templates
     *
     * @return this
     */
    public Environment setLoadedTemplates(HashMap<String, Template> loadedTemplates) {
        this.loadedTemplates = loadedTemplates;

        return this;
    }

    /**
     * Add a new twig4j extension
     *
     * @param extension The extension
     *
     * @return this
     */
    public Environment addExtension(Extension extension) {
        extensions.add(extension);

        return this;
    }

    /**
     * Adds a filter to the environment
     *
     * @param filter The filter to add
     *
     * @return this
     */
    public Environment addFilter(Filter filter) {
        filters.put(filter.getName(), filter);

        return this;
    }

    /**
     * Get a filter from its name
     *
     * @param name The filter name
     *
     * @return The filter
     */
    public Filter getFilter(String name) {
        return filters.get(name);
    }
}
