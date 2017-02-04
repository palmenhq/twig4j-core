package org.twig4j.core.syntax.parser.node.type.control;

import org.twig4j.core.syntax.parser.node.Node;

public class ElseIfBody extends Node {
    public ElseIfBody(Node conditional, Node body, Integer line) {
        super(line);

        nodes.add(0, conditional);
        nodes.add(1, body);
    }
}
