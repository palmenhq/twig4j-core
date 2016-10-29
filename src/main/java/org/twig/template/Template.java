package org.twig.template;

import org.twig.Environment;
import org.twig.exception.TwigRuntimeException;

import java.util.HashMap;

abstract public class Template {
    protected Environment environment;

    public String render() throws TwigRuntimeException {
        return render(new HashMap<>());
    }

    public String render(HashMap<String, ?> context) throws TwigRuntimeException {
        return doRender(context);
    }

    abstract protected String doRender(HashMap<String, ?> context) throws TwigRuntimeException;

    /**
     * Get the template file name
     *
     * @return The file name
     */
    abstract public String getTemplateName();

    /**
     * Get a variable from the provided context
     *
     * @param context The context to get the variable from
     * @param item The variable name
     * @param ignoreStrictChecks Whether to throw an error or just fail silently and return empty string
     * @return
     */
    protected Object getContext(HashMap<String, ?> context, String item, boolean ignoreStrictChecks, Integer line) throws TwigRuntimeException {
        if (!context.containsKey(item)) {
            if (ignoreStrictChecks || !environment.isStrictVariables()) {
                return "";
            } else {
                throw TwigRuntimeException.variableDoesNotExist(item, getTemplateName(), line);
            }
        }

        return context.get(item);
    }

    /**
     * Set the environment
     * @param environment The environment
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
