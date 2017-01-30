package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.Output;

public class PrintExpression extends Node implements Output {
    public PrintExpression(Node expression, Integer line) {
        super(line);
        addNode(expression);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
                .addDebugInfo(this)
                .write("output = output.concat(String.valueOf(")
                    .subCompile(getExpression())
                .writeRaw("));\n");
    }

    public Node getExpression() {
        return nodes.get(0);
    }
}
