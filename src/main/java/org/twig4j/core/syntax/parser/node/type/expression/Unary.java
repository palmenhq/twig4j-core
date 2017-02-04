package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

abstract public class Unary extends Expression {
    public Unary(Node node, Integer line) {
        super(line);

        addNode(node);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler.writeRaw(" ");
        compileOperator(compiler);
        compiler.subCompile(getNode(0));
    }

    /**
     * Write the operator between the 2 nodes
     * @param compiler The compiler
     * @return this
     */
    abstract protected Unary compileOperator(ClassCompiler compiler);
}
