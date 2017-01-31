package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

public class BinaryNotIn extends Binary {
    public BinaryNotIn(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.writeRaw("(! (org.twigjava.extension.Core.inFilter(");
        getLeftNode().compile(compiler);
        compiler.writeRaw(", ");
        getRightNode().compile(compiler);
        compiler.writeRaw(")) )");
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case
        return this;
    }
}
