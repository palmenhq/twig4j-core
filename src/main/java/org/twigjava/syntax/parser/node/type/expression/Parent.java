package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;

public class Parent extends Expression {
    public Parent(Integer line) {
        super(line);
    }

    public Parent(String name, Integer line) {
        super(line);

        putAttribute("output", false);
        putAttribute("name", name);
    }

    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.writeRaw("displayParentBlock(\"" + getAttribute("name") + "\", context, blocks)");
    }
}
