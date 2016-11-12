package org.twig.syntax.parser.node.type;

import org.twig.compiler.*;
import org.twig.syntax.parser.node.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class Text extends Node {
    public Text(ArrayList<Node> nodes, HashMap<String, Object> attributes, Integer line, String tag) {
        super(nodes, attributes, line, tag);
    }

    public Text(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) {
        String text = String.valueOf(attributes.get("data"));
        compiler.write("output = output.concat(");
        compiler.writeString(text);
        compiler.writeRaw(");\n");
    }
}
