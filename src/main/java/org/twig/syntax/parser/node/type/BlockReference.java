package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.Output;

public class BlockReference extends Node implements Output {
    public BlockReference(String name, Integer line, String tag) {
        super(line);
        setTag(tag);

        putAttribute("name", name);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
            .addDebugInfo(this)
            .writeLine("output = output + displayBlock(\"" + getAttribute("name") + "\", context, blocks);");
    }
}
