package org.twig4j.core.syntax.parser.node.type.control;

import org.twig4j.core.syntax.parser.node.Node;

public class ElseBody extends Node {
    public ElseBody(Node body, Integer line) {
        super(line);

        nodes.add(body);
    }
}
