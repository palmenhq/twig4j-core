package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryConcat extends Binary {
    public BinaryConcat(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.writeRaw("(String.valueOf(");
        getLeftNode().compile(compiler);
        compiler.writeRaw(").concat(String.valueOf(");
        getRightNode().compile(compiler);
        compiler.writeRaw(")))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case
        return this;
    }
}
