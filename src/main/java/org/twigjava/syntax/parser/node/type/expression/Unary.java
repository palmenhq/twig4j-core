package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

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
