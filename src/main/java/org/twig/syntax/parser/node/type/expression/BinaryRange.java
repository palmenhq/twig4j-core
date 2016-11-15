package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryRange extends Binary {
    public BinaryRange(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.writeRaw("(org.twig.util.Php.range(");
        getLeftNode().compile(compiler);
        compiler.writeRaw(", ");
        getRightNode().compile(compiler);
        compiler.writeRaw("))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case
        return this;
    }
}
