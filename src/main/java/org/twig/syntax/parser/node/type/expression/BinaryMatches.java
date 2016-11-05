package org.twig.syntax.parser.node.type.expression;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;

public class BinaryMatches extends Binary {
    public BinaryMatches(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
            .writeRaw("(org.twig.util.Php.preg_match(")
            .subCompile(getRightNode())
            .writeRaw(", ")
            .subCompile(getLeftNode())
            .writeRaw(").equals(\"1\"))");
//        ((new com.caucho.quercus.script.QuercusScriptEngineFactory()).getScriptEngine().eval("<?php return preg_match(" + String.valueOf("Foobar").replace("'", "\\'") + ", " + String.valueOf("/[a-z]+/").replace("'", "\\'") + ");").toString().equals("1"))            ));
    }

    @Override
    protected Binary compileOperator(ClassCompiler compiler) {
        // Do nothing in this case

        return this;
    }
}
