package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryEquals extends BinaryDynamicTypeOperation {
    public BinaryEquals(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        compiler.writeRaw("equals");
        return this;
    }
}
