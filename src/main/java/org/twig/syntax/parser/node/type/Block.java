package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class Block extends Node {
    public Block(String name, Node body, Integer line, String tag) {
        super(line);
        setTag(tag);

        addNode(body);
        putAttribute("name", name);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
            .addDebugInfo(this)
            .writeLine("protected String block_" + getAttribute("name") + "(Context context) throws TwigException {")
            .indent()
                .writeLine("java.util.Map<String, Object> tmpForParent;")
                .writeLine("String output = \"\";")
                .subCompile(getNode(0))
                .writeLine("return output;")
            .unIndent()
            .writeLine("}\n");
    }
}
