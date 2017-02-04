package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;

public class Hash extends Expression {
    public Hash(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler.writeRaw("((new org.twig4j.core.util.HashMap())");

        for (String key : attributes.keySet()) {


            Expression expr = (Expression) attributes.get(key);

            compiler.writeRaw(".put(")
                    .writeString(key)
                    .writeRaw(", ")
                    .subCompile(expr)
                    .writeRaw(")");
        }

        compiler.writeRaw(")");
    }
}
