package org.twigjava.syntax.parser.node.type;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.Output;

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
            .writeLine("output = output.concat(displayBlock(\"" + getAttribute("name") + "\", context, blocks, true));");
    }
}
