package org.twigjava.syntax.parser.node.type.control;

import org.twigjava.syntax.parser.node.Node;

public class ElseIfBody extends Node {
    public ElseIfBody(Node conditional, Node body, Integer line) {
        super(line);

        nodes.add(0, conditional);
        nodes.add(1, body);
    }
}
