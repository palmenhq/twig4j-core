package org.twig;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.RuntimeTemplateCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.extension.Core;
import org.twig.extension.Extension;
import org.twig.filter.Filter;
import org.twig.loader.Loader;
import org.twig.syntax.Lexer;
import org.twig.syntax.TokenStream;
import org.twig.syntax.operator.Operator;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.Module;
import org.twig.syntax.parser.tokenparser.AbstractTokenParser;
import org.twig.template.Context;
import org.twig.template.Template;

import javax.xml.bind.DatatypeConverter;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.*;

public class Environment {
    private String templatePackage = "org.twig.template";
    private String templateClassPrefix = "Template_";
    private String templateBaseClass = "org.twig.template.Template";
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
     * Initializes the extensions required for twig
     */
    private void init() {
        addExtension(new Core());
    }

    /**
     * @see this#render(String, Context)
     * Defaults context to empty context
     *
     * @param name
     * @return
     * @throws LoaderException
     * @throws TwigRuntimeException
     * @throws TwigException
     */
    public String render(String name) throws LoaderException, TwigRuntimeException, TwigException {
        return loadTemplate(name).render();
    }

    /**
     * Loads a template and call's it's render method.
     * @see Template#render(Context)
     * @see this#loadTemplate(String)
     *
     * @param name The name of the template to render (defaults index to 0)
     * @param context A map of the variables to provide in the template
     * @return The rendered contents as a string
     * @throws LoaderException If the template is not found
     * @throws TwigRuntimeException On any runtime errors, i.e. trying to access a variable that doesn't exist
     * @throws TwigException On any unknown errors
     */
    public String render(String name, Context context) throws LoaderException, TwigRuntimeException, TwigException {
        return loadTemplate(name).render(context);
    }

    /**
     * Loads a singe template
     *
     * @see this#loadTemplate(String)
     *
     * @param templateName The template name to resolve
     * @return The template object
     * @throws LoaderException
     * @throws TwigException
     */
    public Template resolveTemplate(String templateName) throws LoaderException, TwigException {
        return resolveTemplate(Arrays.asList(templateName));
    }

    /**
     * Loads the first template of the list
     *
     * @param templates The list of templates to load
     * @return The first resolved template in the list
     * @throws LoaderException
     * @throws TwigException
     */
    public Template resolveTemplate(List<String> templates) throws LoaderException, TwigException {
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
     * @see this#loadTemplate(String, Integer)
     * Defaults index to 0
     *
     * @param name
     * @return
     * @throws LoaderException
     * @throws TwigRuntimeException
     * @throws TwigException
     */
    public Template loadTemplate(String name) throws LoaderException, TwigRuntimeException, TwigException {
        return loadTemplate(name, 0);
    }

    /**
     * Resolved a template (from cache or provided loader), and returns a template object ready to render
     *
     * @param name The name of the template
     * @param index The index TODO find out what this is
     * @return The template object
     * @throws LoaderException If the template fails to load
     * @throws TwigRuntimeException On other errors, such as template compilation errors
     * @throws TwigException On unknown errors
     */
    public Template loadTemplate(String name, Integer index) throws LoaderException, TwigRuntimeException, TwigException {
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
            Template template = (Template) Class.forName(fullTemplateClassName).newInstance();
            template.setEnvironment(this);

            return template;
        } catch (ClassNotFoundException e) {
            String javaSourceCode = compileSource(getLoader().getSource(name), name);
            Template template = this.runtimeTemplateCompiler.compile(javaSourceCode, fullTemplateClassName);
            template.setEnvironment(this);

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

        return getTemplateClassPrefix().concat(hashText(key)).concat("_").concat(index.toString());
    }

    /**
     * Create a sha 256 hash from something
     *
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
     */
    protected Environment initExtensions() {
        if (hasInitedExtensions) {
            return this;
        }

        hasInitedExtensions = true;

        extensions.forEach(this::initExtension);

        return this;
    }

    protected void initExtension(Extension extension) {

        // Operators
        this.unaryOperators.putAll(extension.getUnaryOperators());
        this.binaryOperators.putAll(extension.getBinaryOperators());

        // Token parsers
        for (AbstractTokenParser tokenParser : extension.getTokenParsers()) {
            this.tokenParsers.put(tokenParser.getTag(), tokenParser);
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
     * Get the template class prefix (default Template_)
     * @return
     */
    public String getTemplateClassPrefix() {
        return templateClassPrefix;
    }

    /**
     * Set the template class prefix
     * @param templateClassPrefix
     */
    public void setTemplateClassPrefix(String templateClassPrefix) {
        this.templateClassPrefix = templateClassPrefix;
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

    public boolean isStrictVariables() {
        return strictVariables;
    }

    public Environment enableStrictVariables() {
        strictVariables = true;

        return this;
    }

    public Environment disableStrictVariables() {
        strictVariables = false;

        return this;
    }

    public boolean isStrictTypes() {
        return strictTypes;
    }

    public Environment enableStrictTypes() {
        strictTypes = true;

        return this;
    }

    public Environment disableStrictTypes() {
        strictTypes = false;

        return this;
    }

    public boolean isDebug() {
        return debug;
    }

    public Environment enableDebug() {
        debug = true;

        return this;
    }

    public Environment disableDebug() {
        debug = false;

        return this;
    }

    public LinkedHashMap<String, Operator> getBinaryOperators() {
        if (!hasInitedExtensions) {
            initExtensions();
        }

        return binaryOperators;
    }

    public Environment addBinaryOperator(String name, Operator operator) {
        binaryOperators.put(name, operator);

        return this;
    }

    public LinkedHashMap<String, Operator> getUnaryOperators() {
        if (!hasInitedExtensions) {
            initExtensions();
        }

        return unaryOperators;
    }

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

    /**
     * Add a new twig extension
     *
     * @param extension The extension
     * @return this
     */
    public Environment addExtension(Extension extension) {
        extensions.add(extension);

        return this;
    }

    /**
     * Adds a filter to the environment
     * @param filter The filter to add
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
     * @return
     */
    public Filter getFilter(String name) {
        return filters.get(name);
    }
}
