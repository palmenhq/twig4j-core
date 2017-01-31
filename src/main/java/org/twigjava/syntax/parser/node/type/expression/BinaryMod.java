package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

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
