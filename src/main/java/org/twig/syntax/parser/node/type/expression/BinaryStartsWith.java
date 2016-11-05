package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryStartsWith extends Binary {
    public BinaryStartsWith(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
            .writeRaw("(String.valueOf(")
            .subCompile(getLeftNode())
            .writeRaw(").startsWith(String.valueOf(")
            .subCompile(getRightNode())
            .writeRaw(")))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case

        return this;
    }
}
