package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

abstract public class Binary extends Expression {
    public Binary(Node left, Node right, Integer line) {
        super(line);

        addNode(0, left);
        addNode(1, right);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
                .writeRaw("(")
                .subCompile(getLeftNode())
                .writeRaw(" ");

        compileOperator(compiler);

        compiler
                .writeRaw(" ")
                .subCompile(getRightNode())
                .writeRaw(")");
    }

    /**
     * Write the operator between the 2 nodes
     * @param compiler The compiler
     * @return this
     */
    abstract protected Binary compileOperator(ClassCompiler compiler);

    /**
     * Get the node on the left
     * @return The node
     */
    public Node getLeftNode() throws TwigRuntimeException {
        try {
            return getNode(0);
        } catch (TwigRuntimeException e) {
            throw new TwigRuntimeException("Trying to access left node when not set", e);
        }
    }

    /**
     * Get the node on the left
     * @return The node
     */
    public Node getRightNode() throws TwigRuntimeException {
        try {
            return getNode(1);
        } catch (TwigRuntimeException e) {
            throw new TwigRuntimeException("Trying to access right node when not set", e);
        }
    }
}
