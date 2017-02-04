package org.twig4j.core.compiler;

import net.openhft.compiler.CachedCompiler;
import net.openhft.compiler.CompilerUtils;
import org.twig4j.core.Environment;
import org.twig4j.core.exception.TwigException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.template.Template;

public class RuntimeTemplateCompiler {
    private CachedCompiler cachedCompiler = CompilerUtils.CACHED_COMPILER;
    private Environment environment;

    public RuntimeTemplateCompiler(Environment environment) {
        this.environment = environment;
    }

    public RuntimeTemplateCompiler(CachedCompiler cachedCompiler) {
        this.cachedCompiler = cachedCompiler;
    }

    public RuntimeTemplateCompiler(Environment environment, CachedCompiler cachedCompiler) {
        this.environment = environment;
        this.cachedCompiler = cachedCompiler;
    }

    /**
     * Compile a template class from java source code
     *
     * @param sourceCode The java code to compile
     * @param name The name of the template class INCLUDING package name (ie org.twig4j.core.template.02ntueh0k2b20rckb9940ntqb_0)
     *
     * @return The compiled template
     *
     * @throws TwigException If something goes wrong, probably (but hopefully not) because of java syntax error
     */
    public Template compile(String sourceCode, String name) throws TwigException {
        try {
            if (environment.isDebug()) {
                System.out.println("Compiling template " + name);
                System.out.print(sourceCode);

            }

            // First try to load template with Environment constructor, if that doesn't work load it with default constructor
            try {
                Template template = (Template) cachedCompiler.loadFromJava(name, sourceCode).getConstructor(Environment.class).newInstance(environment);

                return template;
            } catch (NoSuchMethodException e) {
                Template template = (Template) cachedCompiler.loadFromJava(name, sourceCode).newInstance();
                template.setEnvironment(environment);

                return template;
            }
        } catch (ClassNotFoundException e) {
            throw new TwigRuntimeException("Failed to find compiled class " + name + ". Maybe it failed to compile?", e);
        } catch (Exception e) {
            if (e.getCause() instanceof TwigException) {
                throw (TwigException)e.getCause();
            }

            throw new TwigRuntimeException("Exception " + e.toString() + " thrown by compiler when compiling template " + name + ".", e);
        }
    }

    /**
     * Get the actual compiler
     *
     * @return The compiler
     */
    public CachedCompiler getCachedCompiler() {
        return cachedCompiler;
    }

    /**
     * Set the actual compiler
     *
     * @param cachedCompiler The compiler
     *
     * @return this
     */
    public RuntimeTemplateCompiler setCachedCompiler(CachedCompiler cachedCompiler) {
        this.cachedCompiler = cachedCompiler;

        return this;
    }

    /**
     * Set the twig environment
     *
     * @param environment The twig environment
     *
     * @return this
     */
    public RuntimeTemplateCompiler setEnvironment(Environment environment) {
        this.environment = environment;

        return this;
    }
}
