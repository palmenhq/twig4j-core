package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryMod extends Binary {
    public BinaryMod(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
                .writeRaw("((Integer)")
                .subCompile(getLeftNode())
                .writeRaw(" ");

        compileOperator(compiler);

        compiler
                .writeRaw(" ((Integer)")
                .subCompile(getRightNode())
                .writeRaw("))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        compiler.writeRaw(" % ");

        return this;
    }
}
