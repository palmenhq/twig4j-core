package org.twig.filter;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public class Filter {
    // The name of the filter
    private String name;
    // Whatever to call when the filter's invoked
    private Function<Object, Object> function;
    // The options
    private Options options = new Options();

    public Filter(String name, Function<Object, Object> function) {
        this.name = name;
        this.function = function;
    }

    public Filter(String name, Function<Object, Object> function, Options options) {
        this.name = name;
        this.function = function;
        this.options = options;
    }

    public String getName() {
        return name;
    }

    public Filter setName(String name) {
        this.name = name;
        return this;
    }

    public Function<Object, Object> getFunction() {
        return function;
    }

    public Filter setFunction(Function<Object, Object> function) {
        this.function = function;
        return this;
    }

    public Options getOptions() {
        return options;
    }

    public Filter setOptions(Options options) {
        this.options = options;
        return this;
    }

    public class Options {
        // Whether the filter needs the environment to work
        private Boolean needsEnvironment = false;
        // Whether the filter needs the context to work
        private Boolean needsContext = false;
        // TODO find out what this is
        private Boolean isVariadic = false;
        // TODO find out what this is
        private Boolean isSafe = false;
        // TODO find out what this is
        private Boolean isSafeCallback = false;
        // TODO find out what this is
        private Boolean preEscape = false;
        // TODO find out what this is
        private Boolean prevervesSafety = false;
        // Which node class to use when compiling filter
        private Class nodeClass = org.twig.syntax.parser.node.type.expression.Filter.class;
        // If the filter is deprecated
        private Boolean deprecated = false;
        // Alternative filter when the filter is deprecated
        private String alternative;

        public Boolean needsEnvironment() {
            return needsEnvironment;
        }

        public Options setNeedsEnvironment(Boolean needsEnvironment) {
            this.needsEnvironment = needsEnvironment;
            return this;
        }

        public Boolean needsContext() {
            return needsContext;
        }

        public Options setNeedsContext(Boolean needsContext) {
            this.needsContext = needsContext;
            return this;
        }

        public Boolean isVariadic() {
            return isVariadic;
        }

        public Options setVariadic(Boolean variadic) {
            isVariadic = variadic;
            return this;
        }

        public Boolean isSafe() {
            return isSafe;
        }

        public Options setSafe(Boolean safe) {
            isSafe = safe;
            return this;
        }

        public Boolean isSafeCallback() {
            return isSafeCallback;
        }

        public Options setSafeCallback(Boolean safeCallback) {
            isSafeCallback = safeCallback;
            return this;
        }

        public Boolean isPreEscape() {
            return preEscape;
        }

        public Options setPreEscape(Boolean preEscape) {
            this.preEscape = preEscape;
            return this;
        }

        public Boolean prevervesSafety() {
            return prevervesSafety;
        }

        public Options setPrevervesSafety(Boolean prevervesSafety) {
            this.prevervesSafety = prevervesSafety;
            return this;
        }

        public Class getNodeClass() {
            return nodeClass;
        }

        public Options setNodeClass(Class nodeClass) {
            this.nodeClass = nodeClass;
            return this;
        }

        public Boolean isDeprecated() {
            return deprecated;
        }

        public Options setDeprecated(Boolean deprecated) {
            this.deprecated = deprecated;
            return this;
        }

        public String getAlternative() {
            return alternative;
        }

        public Options setAlternative(String alternative) {
            this.alternative = alternative;
            return this;
        }
    }
}
