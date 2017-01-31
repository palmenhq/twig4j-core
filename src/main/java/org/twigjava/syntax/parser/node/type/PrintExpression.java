package org.twigjava.syntax.parser.node.type;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.Output;

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
