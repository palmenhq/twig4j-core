package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.syntax.parser.node.Node;

abstract public class Binary extends Expression {
    public Binary(Node left, Node right, Integer line) {
        super(line);

        addNode(0, left);
        addNode(1, right);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
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
    public Node getLeftNode() {
        return getNode(0);
    }

    /**
     * Get the node on the left
     * @return The node
     */
    public Node getRightNode() {
        return getNode(1);
    }
}
