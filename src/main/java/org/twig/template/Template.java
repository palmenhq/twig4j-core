package org.twig.template;

import org.twig.Environment;
import org.twig.exception.TwigRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

abstract public class Template {
    protected Environment environment;

    public Template() {
    }

    public Template(Environment environment) {
        this.environment = environment;
    }

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

    protected Object getAttribute(Object object, String item, List<Object> arguments, String type) throws TwigRuntimeException {
        return getAttribute(object, item, arguments, type, false, false);
    }

    protected Object getAttribute(Object object, String item, List<Object> arguments, String type, boolean isDefinedTest, boolean ignoreStrictChecks) throws TwigRuntimeException {
        if (!type.equals("method")) {
            // TODO the array thing
        }

        if (object == null) {
            if (ignoreStrictChecks) {
                return null;
            } else {
                throw new TwigRuntimeException("Impossible to invoke a method (\"" + item + "\") on a null variable", getTemplateName(), -1);
            }
        }

        // TODO do the object property check

        // TODO check for get*, is*, has*

        List<Class> argumentClasses = new ArrayList<>();
        for (Object argument : arguments) {
            argumentClasses.add(argument.getClass());
        }

        try {
            Method methodToInvoke = object.getClass().getDeclaredMethod(item, argumentClasses.toArray(new Class[argumentClasses.size()]));

            return methodToInvoke.invoke(object, arguments.toArray());
        } catch (NoSuchMethodException e) {
            throw new TwigRuntimeException(
                    "No such method \"" + item + "\" on object of type \"" + object.getClass().getName() + "\"",
                    getTemplateName(),
                    -1,
                    e
            );
        } catch (IllegalAccessException e) {
            throw new TwigRuntimeException(
                    "Call to inaccessible method \"" + item + "\" on object of type \"" + object.getClass().getName() + "\"",
                    getTemplateName(),
                    -1,
                    e
            );
        } catch (InvocationTargetException e) {
            throw new TwigRuntimeException(
                    "Method \"" + object.getClass().getName() + "#" + item + "()\" threw exception " + e.getCause().getClass().getName() + " \"" + e.getCause().getMessage() + "\"",
                    getTemplateName(),
                    -1,
                    e.getCause()
            );
        }
    }

    protected boolean compare(Object a, Object b) throws TwigRuntimeException {
        if (!a.getClass().equals(b.getClass())) {
            if (environment.isStrictTypes()) {
                throw new TwigRuntimeException(
                        String.format("Cannot compare different types (tried to compare \"%s\" with \"%s\")", a.getClass().getName(), b.getClass().getName()),
                        getTemplateName(),
                        -1
                );
            } else {
                return false;
            }

        }

        return a.equals(b);
    }

    /**
     * Set the environment
     * @param environment The environment
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }
}
