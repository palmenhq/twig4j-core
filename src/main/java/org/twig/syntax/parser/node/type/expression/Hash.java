package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class Hash extends Expression {
    public Hash(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException{
        compiler.writeRaw("((new org.twig.util.HashMap())");

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
