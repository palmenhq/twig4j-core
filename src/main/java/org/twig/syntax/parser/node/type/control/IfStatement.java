package org.twig.syntax.parser.node.type.control;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

import java.util.HashMap;
import java.util.List;

public class IfStatement extends Node {
    public IfStatement(List<Node> nodes, Integer line, String tag) {
        super(nodes, new HashMap<>(), line, tag);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.addDebugInfo(this);

        for (Node node : nodes) {
            if (node instanceof IfBody) {
                compiler
                        .write("if ((Boolean) ")
                        .subCompile(node.getNode(0))
                        .writeRaw(") {\n")
                        .subCompile(node.getNode(1));
            } else if (node instanceof ElseIfBody) {
                compiler
                        .write("} else if ((Boolean) ")
                        .subCompile(node.getNode(0))
                        .writeRaw(") {\n")
                        .subCompile(node.getNode(1));
            } else if (node instanceof ElseBody) {
                compiler
                        .write("} else {\n")
                        .subCompile(node.getNode(0));
            }
        }

        compiler.writeLine("}");
    }
}
