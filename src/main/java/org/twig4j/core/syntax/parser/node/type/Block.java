package org.twig4j.core.syntax.parser.node.type;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class Block extends Node {
    public Block(String name, Node body, Integer line, String tag) {
        super(line);
        setTag(tag);

        addNode(body);
        putAttribute("name", name);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler
            .addDebugInfo(this)
            .writeLine("public String block_" + getAttribute("name") + "(Context context, java.util.Map<String, TemplateBlockMethodSet> blocks) throws Twig4jException {")
            .indent()
                .writeLine("java.util.Map<String, Object> tmpForParent;")
                .writeLine("StringBuilder output = new StringBuilder();")
                .subCompile(getNode(0))
                .writeLine("return output.toString();")
            .unIndent()
            .writeLine("}\n");
    }
}
