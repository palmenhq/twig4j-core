package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

abstract public class Binary extends Expression {
    public Binary(Node left, Node right, Integer line) {
        super(line);

        addNode(0, left);
        addNode(1, right);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
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
     *
     * @return The node
     *
     * @throws Twig4jRuntimeException When trying to access a node that doesn't exist
     */
    public Node getLeftNode() throws Twig4jRuntimeException {
        try {
            return getNode(0);
        } catch (Twig4jRuntimeException e) {
            throw new Twig4jRuntimeException("Trying to access left node when not set", e);
        }
    }

    /**
     * Get the node on the left
     *
     * @return The node
     *
     * @throws Twig4jRuntimeException When trying to access a node that doesn't exist
     */
    public Node getRightNode() throws Twig4jRuntimeException {
        try {
            return getNode(1);
        } catch (Twig4jRuntimeException e) {
            throw new Twig4jRuntimeException("Trying to access right node when not set", e);
        }
    }
}
