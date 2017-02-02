package org.twigjava.syntax.parser.node.type;

import org.twigjava.compiler.*;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.Output;

import java.util.ArrayList;
import java.util.HashMap;

public class Text extends Node implements Output {
    public Text(ArrayList<Node> nodes, HashMap<String, Object> attributes, Integer line, String tag) {
        super(nodes, attributes, line, tag);
    }

    public Text(String data, Integer line) {
        super(line);
        putAttribute("data", data);
    }

    public Text(Integer line) {
        super(line);
    }

    @Override
    public void compile(ClassCompiler compiler) {
        String text = String.valueOf(attributes.get("data"));
        compiler.write("output.append(");
        compiler.writeString(text);
        compiler.writeRaw(");\n");
    }
}
