package org.twig.template;

import org.twig.Environment;
import org.twig.exception.TwigRuntimeException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public String render(Map<String, ?> context) throws TwigRuntimeException {
        return doRender(context);
    }

    abstract protected String doRender(Map<String, ?> context) throws TwigRuntimeException;

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
    protected Object getContext(Map<String, ?> context, String item, boolean ignoreStrictChecks, Integer line) throws TwigRuntimeException {
        if (!context.containsKey(item)) {
            if (ignoreStrictChecks || !environment.isStrictVariables()) {
                return "";
            } else {
                throw TwigRuntimeException.variableDoesNotExist(item, getTemplateName(), line);
            }
        }

        return context.get(item);
    }

    protected Object getAttribute(Object object, Object item, List<Object> arguments, String type) throws TwigRuntimeException {
        return getAttribute(object, item, arguments, type, false, false);
    }

    protected Object getAttribute(Object object, Object item, List<Object> arguments, String type, boolean isDefinedTest, boolean ignoreStrictChecks) throws TwigRuntimeException {
        if (!type.equals("method")) {
            Integer arrayItem;
            // Cast bools and floats/doubles to integers
            if (item.getClass() == boolean.class) {
                arrayItem = (boolean)item ? 1 : 0;
            } else {
                arrayItem = (Integer) item;
            }

            if (object instanceof List) {
                try {
                    return ((List) object).get(arrayItem);
                } catch (IndexOutOfBoundsException e) {
                    // Just continue
                }
            }

            if (type.equals("array")) { // TODO !is_object(object)
                if (isDefinedTest) {
                    return false;
                }

                if (ignoreStrictChecks || !environment.isStrictVariables()) {
                    return null;
                }

                String message;
                if (object instanceof List) {
                    if (((List) object).size() == 0) {
                        message = String.format("Key \"%s\" does not exist as the array is empty", String.valueOf(item));
                    } else {
                        message = String.format("Key \"%s\" for array with keys \"%s\" does not exist", String.valueOf(item), String.join(", ", (List) object));
                    }
                } else {
                    if (object == null) {
                        message = String.format("Impossible to access a key (\"%s\") on a null variable", String.valueOf(item));
                    } else {
                        message = String.format("Impossible to access an attribute (\"%s\") on a %s variable", String.valueOf(item), object.getClass().getName());
                    }
                }

                throw new TwigRuntimeException(message, getTemplateName(), -1);
            }
        }

        if (object == null) {
            if (ignoreStrictChecks) {
                return null;
            } else {
                throw new TwigRuntimeException("Impossible to invoke a method (\"" + String.valueOf(item) + "\") on a null variable", getTemplateName(), -1);
            }
        }

        // TODO do the object property check

        // TODO check for get*, is*, has*

        List<Class> argumentClasses = new ArrayList<>();
        for (Object argument : arguments) {
            argumentClasses.add(argument.getClass());
        }

        try {
            Method methodToInvoke = object.getClass().getDeclaredMethod(String.valueOf(item), argumentClasses.toArray(new Class[argumentClasses.size()]));

            return methodToInvoke.invoke(object, arguments.toArray());
        } catch (NoSuchMethodException e) {
            throw new TwigRuntimeException(
                    "No such method \"" + String.valueOf(item) + "\" on object of type \"" + object.getClass().getName() + "\"",
                    getTemplateName(),
                    -1,
                    e
            );
        } catch (IllegalAccessException e) {
            throw new TwigRuntimeException(
                    "Call to inaccessible method \"" + String.valueOf(item) + "\" on object of type \"" + object.getClass().getName() + "\"",
                    getTemplateName(),
                    -1,
                    e
            );
        } catch (InvocationTargetException e) {
            throw new TwigRuntimeException(
                    "Method \"" + object.getClass().getName() + "#" + String.valueOf(item) + "()\" threw exception " + e.getCause().getClass().getName() + " \"" + e.getCause().getMessage() + "\"",
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
