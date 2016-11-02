package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryFloorDivide extends Binary {
    public BinaryFloorDivide(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
            .write("((int) Math.floor( ((double)")
            .subCompile(getLeftNode())
            .writeRaw(") / ")
            .subCompile(getRightNode())
            .writeRaw(" ))");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case

        return this;
    }
}
