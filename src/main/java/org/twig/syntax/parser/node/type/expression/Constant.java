package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;

public class Constant extends Expression {
    public Constant(Integer line) {
        super(line);
    }

    public Constant(Object value, Integer line) {
        super(line);

        putAttribute("data", value);
    }

    public void compile(ClassCompiler compiler) {
        compiler.representValue(attributes.get("data"));
    }
}
