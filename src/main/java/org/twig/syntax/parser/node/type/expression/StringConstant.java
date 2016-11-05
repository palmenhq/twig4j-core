package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;

public class StringConstant extends Constant {
    public StringConstant(Integer line) {
        super(line);
    }

    public StringConstant(String value, Integer line) {
        super(line);

        putAttribute("data", value);
    }

    public void compile(ClassCompiler compiler) {
        compiler.writeString(attributes.get("data"));
    }
}
