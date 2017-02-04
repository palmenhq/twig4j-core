package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;

public class StringConstant extends Constant {
    public StringConstant(Integer line) {
        super(line);
    }

    public StringConstant(String value, Integer line) {
        super(line);

        putAttribute("data", value);
    }

    public void compile(ClassCompiler compiler) {
        compiler.writeString(String.valueOf(attributes.get("data")));
    }
}
