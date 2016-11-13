package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

abstract public class Unary extends Expression {
    public Unary(Node node, Integer line) {
        super(line);

        addNode(node);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
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
