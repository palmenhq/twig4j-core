package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.syntax.parser.node.Node;

import java.util.ArrayList;
import java.util.HashMap;

public class Constant extends Node {
    public Constant(ArrayList<Node> nodes, HashMap<String, String> attributes, Integer line, String tag) {
        super(nodes, attributes, line, tag);
    }

    public Constant(Integer line) {
        super(line);
    }

    public Constant(String value, Integer line) {
        super(line);

        putAttribute("data", value);
    }

    public void compile(ClassCompiler compiler) {
        compiler.representValue(attributes.get("data"));
    }
}
