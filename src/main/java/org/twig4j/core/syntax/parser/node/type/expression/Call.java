package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

import java.lang.reflect.Method;
import java.util.List;

abstract public class Call extends Expression {
    public Call(Integer line) {
        super(line);
    }

    protected void compileCallable(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        Method callable = ((Method)getAttribute("callable"));

        compiler
            .writeRaw(callable.getDeclaringClass().getCanonicalName())
            .writeRaw(".")
            .writeRaw(callable.getName());

        compileArguments(compiler);
    }

    protected void compileArguments(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler.writeRaw("(");
        Boolean first = true;

        if (hasAttribute("needs_environment") && ((Boolean)getAttribute("needs_environment"))) {
            compiler.writeRaw("environment");
            first = false;
        }

        if (hasAttribute("needs_context") && ((Boolean)getAttribute("needs_context"))) {
            if (!first) {
                compiler.writeRaw(", ");
            }
            compiler.writeRaw("context");
        }

        if (hasAttribute("arguments") && getAttribute("arguments") instanceof List) {
            for (String argument : ((List<String>) getAttribute("arguments"))) {
                if (!first) {
                    compiler.writeRaw(", ");
                }
                compiler.writeString(argument);
                first = false;
            }
        }

        // 0 = body node
        if (getNode(0) != null) {
            if (!first) {
                compiler.writeRaw(", ");
            }
            compiler.subCompile(getNode(0));
            first = false;
        }

        // TODO named arguments
        for (Node argument : getNode(2).getNodes()) {
            if (!first) {
                compiler.writeRaw(", ");
            }

            compiler.subCompile(argument);
            first = false;
        }

        compiler.writeRaw(")");
    }
}
