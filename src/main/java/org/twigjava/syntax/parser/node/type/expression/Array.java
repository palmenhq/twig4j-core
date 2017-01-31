package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class Array extends Expression {
    public Array(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException{
        compiler.writeRaw("java.util.Arrays.asList(");

        boolean isFirst = true;
        for (Node node : nodes) {
            if(!isFirst) {
                compiler.writeRaw(", ");
            }
            isFirst = false;

            node.compile(compiler);
        }

        compiler.writeRaw(")");
    }
}
