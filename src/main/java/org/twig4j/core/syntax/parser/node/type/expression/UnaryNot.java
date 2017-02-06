package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class UnaryNot extends Unary {
    public UnaryNot(Node node, Integer line) {
        super(node, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler
            .writeRaw(" !(new org.twig4j.core.typesystem.DynamicType(")
            .subCompile(getNode(0))
            .writeRaw(")).toBoolean()");
    }

    @Override
    protected Unary compileOperator(ClassCompiler compiler) {
        // nothing to do here
        return this;
    }
}
