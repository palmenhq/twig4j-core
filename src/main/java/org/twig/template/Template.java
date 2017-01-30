package org.twig.template;

import org.twig.Environment;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class Template {
    protected Environment environment;
    protected Template parent;
    protected Map<String, TemplateBlockMethodSet> blocks = new HashMap<>();

    public Template() {
    }

    public Template(Environment environment) throws TwigException {
        this.environment = environment;
    }

    /**
     * @see this#render(Context)
     *
     * Defaults Context to empty context
     */
    public String render() throws TwigException {
        return render(new Context());
    }

    /**
     * Returns a string of the rendered template
     *
     * @param context The context - a Map that contains all variables to make available in the template
     * @return Rendered html (or whatever content-type you're rendering)
     * @throws TwigRuntimeException If there are any errors, i.e. accessing a variable that is not in the context.
     */
    public String render(Context context) throws TwigException {
        return display(context, blocks);
    }


    public String display(Context context, Map<String, TemplateBlockMethodSet> blocks) throws TwigException {
        Map<String, TemplateBlockMethodSet> mergedBlocks = new HashMap<>();
        mergedBlocks.putAll(this.blocks);
        mergedBlocks.putAll(blocks);

        return doDisplay(context, mergedBlocks);
    }

    /**
     * The actual rendering of the template, done in the runtime compiled template classes
     *
     * @param context The context that contains all variables
     * @return
     * @throws TwigRuntimeException
     */
    abstract protected String doDisplay(Context context, Map<String, TemplateBlockMethodSet> blocks) throws TwigException;

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

    /**
     * @see this#getAttribute(Object, Object, List, String, boolean, boolean)
     *
     * Defaults to strict checks (which means exceptions are thrown when not able to access the property)
     *
     * @param object The object or array (hopefully) to get the attribute on
     * @param item The attribute to get, a String if an object is passed and an Integer if an array/List is passed
     * @param arguments If this is a method pass the arguments as an array here
     * @param type Whether it's an array or an actual object
     *
     * @return The value
     * @throws TwigRuntimeException If the item/property can not be accessed
     */
    protected Object getAttribute(Object object, Object item, List<Object> arguments, String type) throws TwigRuntimeException {
        return getAttribute(object, item, arguments, type, false, false);
    }

    /**
     * Gets an attribute from an object or array. Object can be a Map<String|Integer|Boolean>, object with the property passed as `item`,
     * an object that has one of the methods `get(Item)`, `is(Item)` or `has(Item)` where (Item) is the string passed to `item`.
     *
     * @param object The object or array (hopefully) to get the attribute on
     * @param item The attribute to get, a String if an object is passed and an Integer if an array/List is passed
     * @param arguments If this is a method pass the arguments as an array here
     * @param type Whether it's an array or an actual object
     * @param isDefinedTest Not sure what this does but it should always be set to false unless you know what you're doing
     * @param ignoreStrictChecks Throw exceptions if property can't be accessed if set to false, just return null if set to true
     *
     * @return The value
     * @throws TwigRuntimeException If the item/property can not be accessed
     */
    protected Object getAttribute(Object object, Object item, List<Object> arguments, String type, boolean isDefinedTest, boolean ignoreStrictChecks) throws TwigRuntimeException {
        if (!type.equals("method")) {

            // If this is a regular array list
            if (object instanceof List) {
                Integer arrayItem = null;
                // Cast bools and floats/doubles to integers
                if (item.getClass() == boolean.class) {
                    arrayItem = (boolean)item ? 1 : 0;
                } else if (item.getClass() == Float.class || item.getClass() == Double.class || item.getClass() == Integer.class) {
                    arrayItem = (Integer) item;
                }

                try {
                    return ((List) object).get(arrayItem);
                } catch (IndexOutOfBoundsException e) {
                    // Just continue
                } catch (NullPointerException e) {
                    // Just continue
                }
            }

            // If this is an array
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

        // Can't work with null variables
        if (object == null) {
            if (ignoreStrictChecks || !environment.isStrictVariables() ) {
                return null;
            } else {
                throw new TwigRuntimeException("Impossible to invoke a method (\"" + String.valueOf(item) + "\") on a null variable", getTemplateName(), -1);
            }
        }

        // Can't work with primitive types
        if (
            object.getClass() == Integer.class
            || object.getClass() == String.class
            || object.getClass() == Float.class
            || object.getClass() == Double.class
            || object.getClass() == Boolean.class
        ) {
            if (ignoreStrictChecks || !environment.isStrictVariables()) {
                return null;
            } else {
                throw new TwigRuntimeException("Impossible to invoke a method (\"" + String.valueOf(item) + "\") on a " + object.getClass().getName() + " variable", getTemplateName(), -1);
            }
        }

        // Get map attribute or "regular" property
        if (!type.equals("method") && !(object instanceof Template)) { // Template does not have public properties, and we don't want to allow access to internal ones
            // Get map attribute
            if (object instanceof Map) {
                if (((Map) object).containsKey(item)) {
                    return ((Map) object).get(item);
                }
            }

            // Try to get the property
            try {
                Field field = object.getClass().getField((String) item);
                return field.get(object);
            } catch (Exception e) {
                // Just continue, there might be a getter
            }
        }

        // Only methods left, setup the arguments
        List<Class> argumentClasses = new ArrayList<>();
        for (Object argument : arguments) {
            argumentClasses.add(argument.getClass());
        }

        // Used for (get|is|has)Property
        String propertyNameWithUpperFirst = ((String) item).substring(0, 1).toUpperCase() + ((String) item).substring(1);

        // 1. Try to invoke the "method" directly (ie `property()`)
        try {
            Method methodToInvoke = object.getClass().getDeclaredMethod(String.valueOf(item), argumentClasses.toArray(new Class[argumentClasses.size()]));

            return methodToInvoke.invoke(object, arguments.toArray());
        } catch (NoSuchMethodException e) {
            // 2. Try a getter (ie `getProperty()`)
            try {
                Method methodToInvoke = object.getClass().getDeclaredMethod("get" + propertyNameWithUpperFirst);

                return methodToInvoke.invoke(object);
            } catch (NoSuchMethodException getterException) {
                // 3. Try a "haser' (ie `hasProperty()`)
                try {
                    Method methodToInvoke = object.getClass().getDeclaredMethod("is" + propertyNameWithUpperFirst);

                    return methodToInvoke.invoke(object);
                } catch (NoSuchMethodException iserException) {
                    // 4. Try an "iser" (ie `isProperty()`)
                    try {
                        Method methodToInvoke = object.getClass().getDeclaredMethod("has" + propertyNameWithUpperFirst);

                        return methodToInvoke.invoke(object);
                    } catch (NoSuchMethodException haserException) {
                        // Property was not a map attribute, class  field, method, getter, haser or iser - we have nothing more to try
                        throw new TwigRuntimeException(
                                "No such method \"" + String.valueOf(item) + "\" on object of type \"" + object.getClass().getName() + "\"",
                                getTemplateName(),
                                -1,
                                e
                        );
                    } catch (IllegalAccessException haserException) {
                        throw TwigRuntimeException.illegalAccessToMethod("has" + propertyNameWithUpperFirst, object.getClass().getName(), getTemplateName(), haserException);
                    } catch (InvocationTargetException haserException) {
                        throw TwigRuntimeException.invocationTargetException("has" + propertyNameWithUpperFirst, object.getClass().getName(), getTemplateName(), haserException);
                    }

                // is
                } catch (IllegalAccessException iserException) {
                    throw TwigRuntimeException.illegalAccessToMethod("is" + propertyNameWithUpperFirst, object.getClass().getName(), getTemplateName(), iserException);
                } catch (InvocationTargetException iserException) {
                    throw TwigRuntimeException.invocationTargetException("is" + propertyNameWithUpperFirst, object.getClass().getName(), getTemplateName(), iserException);
                }

            // get
            } catch (IllegalAccessException getterException) {
                throw TwigRuntimeException.illegalAccessToMethod("get" + propertyNameWithUpperFirst, object.getClass().getName(), getTemplateName(), getterException);
            } catch (InvocationTargetException getterException) {
                throw TwigRuntimeException.invocationTargetException("get" + propertyNameWithUpperFirst, object.getClass().getName(), getTemplateName(), getterException);
            }

        // The method
        } catch (IllegalAccessException e) {
            throw TwigRuntimeException.illegalAccessToMethod(String.valueOf(item), object.getClass().getName(), getTemplateName(), e);
        } catch (InvocationTargetException e) {
            throw TwigRuntimeException.invocationTargetException(String.valueOf(item), object.getClass().getName(), getTemplateName(), e);
        }
    }

    /**
     * Compare 2 variables or throw exception if of different types
     *
     * @param a Object 1
     * @param b Object 2
     * @return Whether they are equal
     * @throws TwigRuntimeException On type errors
     */
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
     * Loads another template
     *
     * @param template The actual template name to load
     * @param requestingTemplateName Whoever requested as template
     * @param line The line it's requested on
     * @param index The template index TODO implement this
     * @return The loaded template
     * @throws TwigException If the included template throws any errors or isn't found
     */
    protected Template loadTemplate(String template, String requestingTemplateName, Integer line, Integer index) throws TwigException {
        try {
            return environment.resolveTemplate(template);
        } catch (TwigException e) {
            if (e.getTemplateName() == null) {
                e.setTemplateName(requestingTemplateName == null ? getTemplateName() : requestingTemplateName);
            }

            if (e.getLineNumber() == null) {
                // TODO guess line number
                e.setLineNumber(line);
            }

            throw e;
        }
    }

    protected String displayBlock(String name, Context context, Map<String, TemplateBlockMethodSet> blocks) throws TwigException {
        // TODO find out what the displayBlock and useBlocks is for

        if (!blocks.containsKey(name)) {
            return parent.displayBlock(name, context, blocks);
        }

        try {
            return blocks.get(name).invoke(name, context);
        } catch (TwigException e) {
            if (e.getTemplateName() == null) {
                e.setTemplateName(getTemplateName());
            }

            // this is mostly useful for Loader exceptions
            // see LoaderException
            if (e.getLineNumber() == null) {
                e.setLineNumber(-1);
                // TODO guess line number
            }

            throw e;
        } catch (Exception e) {
            throw new TwigRuntimeException(
                String.format("An exception has been thrown during the rendering of a template (\"%s\").", e.getMessage()),
                getTemplateName(),
                -1,
                e
            );
        }
    }

    /**
     * Set the environment
     * @param environment The environment
     */
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    public class TemplateBlockMethodSet {
        protected Template template;
        protected Method method;

        public TemplateBlockMethodSet(Template template, Method method) {
            this.template = template;
            this.method = method;
        }

        public String invoke(String name, Context context) throws TwigRuntimeException {
            try {
                return (String)method.invoke(template, context);
            } catch (ReflectiveOperationException e) {
                throw new TwigRuntimeException("Failed displaying block \"" + name + "\" (\"" + e.getMessage() + "\")", getTemplateName(), -1, e);
            }

        }

        public Template getTemplate() {
            return template;
        }

        public TemplateBlockMethodSet setTemplate(Template template) {
            this.template = template;
            return this;
        }

        public Method getMethod() {
            return method;
        }

        public TemplateBlockMethodSet setMethod(Method method) {
            this.method = method;
            return this;
        }
    }
}
