package org.twigjava.syntax.parser.node.type.control;

import org.twigjava.syntax.parser.node.Node;

public class ElseBody extends Node {
    public ElseBody(Node body, Integer line) {
        super(line);

        nodes.add(body);
    }
}
