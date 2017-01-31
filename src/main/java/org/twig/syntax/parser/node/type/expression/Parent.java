package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;

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
