package org.twig4j.core.syntax.parser.node.type;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class ForLoop extends Node {
    public ForLoop(Integer line) {
        super(line);
    }

    public ForLoop(Integer line, String tag) {
        super(line);
        setTag(tag);

        // TODO change attributes to false when we find out where it's changed to true in another place (probably a node visitor)
        putAttribute("with_loop", true);
        putAttribute("ifExpr", true);
        putAttribute("else", true);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        if (((Boolean)getAttribute("else"))) {
            compiler.writeLine("context.put(\"_iterated\", true);");
        }

        if (((Boolean)getAttribute("with_loop"))) {
            compiler
                    .writeLine("((org.twig4j.core.util.HashMap)context.get(\"loop\"))")
                    .indent()
                        .writeLine(".put(\"index\", ((Integer)((org.twig4j.core.util.HashMap)context.get(\"loop\")).get(\"index\")) + 1)")
                        .writeLine(".put(\"index0\", ((Integer)((org.twig4j.core.util.HashMap)context.get(\"loop\")).get(\"index0\")) + 1)")
                        .writeLine(".put(\"first\", false);")
                    .unIndent();
        }

        if (((Boolean)getAttribute("ifExpr"))) {
            compiler
                    .writeLine("if (((org.twig4j.core.util.HashMap)context.get(\"loop\")).containsKey(\"length\")) {")
                    .indent()
                        .writeLine("((org.twig4j.core.util.HashMap)context.get(\"loop\")).put(\"revindex0\", ((Integer)((org.twig4j.core.util.HashMap)context.get(\"loop\")).get(\"revindex0\")) - 1);")
                        .writeLine("((org.twig4j.core.util.HashMap)context.get(\"loop\")).put(\"revindex\", ((Integer)((org.twig4j.core.util.HashMap)context.get(\"loop\")).get(\"revindex\")) - 1);")
                        .writeLine("((org.twig4j.core.util.HashMap)context.get(\"loop\")).put(\"last\", ((Integer)((org.twig4j.core.util.HashMap)context.get(\"loop\")).get(\"revindex0\")) == 0);")
                    .unIndent()
                    .writeLine("}");
        }
    }
}
