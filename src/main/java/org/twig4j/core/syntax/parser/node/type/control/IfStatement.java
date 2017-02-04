package org.twig4j.core.syntax.parser.node.type.control;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

import java.util.HashMap;
import java.util.List;

public class IfStatement extends Node {
    public IfStatement(List<Node> nodes, Integer line, String tag) {
        super(nodes, new HashMap<>(), line, tag);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
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
            } else {
                throw new Twig4jRuntimeException("Invalid node of type \"" + node.getClass().toString() + "\" passed to if statement" +
                        "(expected " + IfBody.class.toString() + ", " + ElseIfBody.class.toString() + " or " + ElseBody.class.toString() + ").", null, line);
            }
        }

        compiler.writeLine("}");
    }
}
