package org.twig4j.core.syntax.parser.node.type;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;
import org.twig4j.core.syntax.parser.node.Output;

public class PrintExpression extends Node implements Output {
    public PrintExpression(Node expression, Integer line) {
        super(line);
        addNode(expression);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler
                .addDebugInfo(this)
                .write("output.append(")
                    .subCompile(getExpression())
                .writeRaw(");\n");
    }

    public Node getExpression() {
        return nodes.get(0);
    }
}
