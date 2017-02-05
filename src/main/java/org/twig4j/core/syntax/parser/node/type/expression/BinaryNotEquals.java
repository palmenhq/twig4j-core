package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryNotEquals extends Binary {
    public BinaryNotEquals(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler
            .writeRaw("(!(new org.twig4j.core.typesystem.DynamicType(")
            .subCompile(getLeftNode())
            .writeRaw(")).equals((new org.twig4j.core.typesystem.DynamicType(")
            .subCompile(getRightNode())
            .writeRaw("))))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case

        return this;
    }
}
