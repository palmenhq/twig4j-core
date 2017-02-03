package org.twigjava.syntax.parser.node.type;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;

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
            .writeLine("public String block_" + getAttribute("name") + "(Context context, java.util.Map<String, TemplateBlockMethodSet> blocks) throws TwigException {")
            .indent()
                .writeLine("java.util.Map<String, Object> tmpForParent;")
                .writeLine("StringBuilder output = new StringBuilder();")
                .subCompile(getNode(0))
                .writeLine("return output.toString();")
            .unIndent()
            .writeLine("}\n");
    }
}
