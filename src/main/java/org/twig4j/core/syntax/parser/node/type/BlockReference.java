package org.twig4j.core.syntax.parser.node.type;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.Output;

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
            .writeLine("output.append(displayBlock(\"" + getAttribute("name") + "\", context, blocks, true));");
    }
}
