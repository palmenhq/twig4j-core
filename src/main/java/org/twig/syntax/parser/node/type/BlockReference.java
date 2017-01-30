package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BlockReference extends Node {
    public BlockReference(String name, Integer line, String tag) {
        super(line);
        setTag(tag);

        putAttribute("name", name);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        super.compile(compiler);

        // TODO
    }
}
