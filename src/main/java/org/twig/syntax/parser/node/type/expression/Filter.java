package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.syntax.parser.node.Node;

public class Filter extends Expression {
    public Filter(Node node, Constant filterName, Node arguments, Integer line, String tag) {
        super(line);
        setTag(tag);

        addNode(node); // 0 = Body node
        addNode(filterName); // 1 = filter name
        addNode(arguments); // 2 = arguments
    }

    public void compile(ClassCompiler compiler) {
        // TODO
    }
}
