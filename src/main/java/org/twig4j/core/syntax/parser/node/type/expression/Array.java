package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class Array extends Expression {
    public Array(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
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
