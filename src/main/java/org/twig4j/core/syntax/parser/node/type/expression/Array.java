package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

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
