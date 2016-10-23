package org.twig.syntax.parser.node.type;

import org.twig.compiler.*;
import org.twig.syntax.parser.node.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class Text extends Node {
    public Text(ArrayList<Node> nodes, HashMap<String, String> attributes, Integer line, String tag) {
        super(nodes, attributes, line, tag);
    }

    public Text(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) {
        String text = attributes.get("data");
        text = escape(text);
        compiler.writeLine("output = output.concat(\"".concat(text).concat("\");"));
    }

    private String escape(String text) {
        return text.replace("\\", "\\\\")
                .replace("\n", "\\n")
                .replace("\"", "\\\"");
    }
}
