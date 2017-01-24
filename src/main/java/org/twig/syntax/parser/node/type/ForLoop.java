package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class ForLoop extends Node {
    public ForLoop(Integer line) {
        super(line);
    }

    public ForLoop(Integer line, String tag) {
        super(line);
        setTag(tag);

        // TODO change attributes to false when we find out where it's changed to true in another place (probably a node visitor)
        putAttribute("with_loop", true);
        putAttribute("ifexpr", true);
        putAttribute("else", true);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        if (((Boolean)getAttribute("with_loop"))) {
            compiler
                    .writeLine("((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"loop\"))")
                    .indent()
                        .writeLine(".put(\"index\", ((Integer)((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"loop\")).get(\"index\")) + 1)")
                        .writeLine(".put(\"index0\", ((Integer)((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"loop\")).get(\"index0\")) + 1)")
                        .writeLine(".put(\"first\", false);")
                    .unIndent();
        }

        // TODO revindex
    }
}
