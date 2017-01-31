package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Output;

public class BlockReferenceExpression extends Expression implements Output {
    public BlockReferenceExpression(Expression name, Integer line, String tag) {
        super(line);
        setTag(tag);

        addNode(name);
        putAttribute("output", false);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.addDebugInfo(this);

        if ((Boolean) getAttribute("output")) {
            compiler
                .writeLine("output = output.concat(displayBlock(")
                   .subCompile(getNode(0))
                .writeRaw(", context, blocks, true));");
        } else {
            compiler
                .writeRaw("displayBlock(")
                    .subCompile(getNode(0))
                .writeRaw(", context, blocks, true)");
        }
    }
}
