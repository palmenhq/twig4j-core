package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Output;

public class BlockReferenceExpression extends Expression implements Output {
    public BlockReferenceExpression(Expression name, Integer line, String tag) {
        super(line);
        setTag(tag);

        addNode(name);
        putAttribute("output", false);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler.addDebugInfo(this);

        if ((Boolean) getAttribute("output")) {
            compiler
                .writeLine("output.append(displayBlock(")
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
