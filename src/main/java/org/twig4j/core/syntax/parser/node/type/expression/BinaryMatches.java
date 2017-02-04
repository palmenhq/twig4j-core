package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class BinaryMatches extends Binary {
    public BinaryMatches(Node left, Node right, Integer line) {
        super(left, right, line);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        compiler
            .writeRaw("(org.twig4j.core.util.Php.preg_match(")
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
