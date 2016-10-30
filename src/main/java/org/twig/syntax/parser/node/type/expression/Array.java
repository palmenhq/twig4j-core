package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.syntax.parser.node.Node;

public class Array extends Expression {
    public Array(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
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
