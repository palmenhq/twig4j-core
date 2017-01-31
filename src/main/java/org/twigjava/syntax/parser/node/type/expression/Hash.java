package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;

public class Hash extends Expression {
    public Hash(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException{
        compiler.writeRaw("((new org.twigjava.util.HashMap())");

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
