package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

import java.util.List;

public class Set extends Node {
    public Set(List<String> names, List<Node> values, Boolean capture, Integer line) {
        super(line);
        nodes = values;
        putAttribute("names", names);
        putAttribute("capture", capture);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        List<String> names = (List<String>)getAttribute("names");

        compiler.addDebugInfo(this);

        for (Integer variableIndex = 0; variableIndex < names.size(); variableIndex ++) {
            compiler
                .write("context.put(")
                .writeString(names.get(variableIndex))
                .writeRaw(", ")
                .subCompile(nodes.get(variableIndex))
                .writeRaw(");\n");
        }

    }
}
