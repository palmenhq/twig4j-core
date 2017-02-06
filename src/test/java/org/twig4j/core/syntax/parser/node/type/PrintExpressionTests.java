package org.twig4j.core.syntax.parser.node.type;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.type.expression.Constant;

public class PrintExpressionTests {
    @Test
    public void canCompile() throws LoaderException, Twig4jRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());
        Constant constant = new Constant(1, 1);
        constant.addNode(constant);
        PrintExpression print = new PrintExpression(constant, 1);

        print.compile(compiler);

        Assert.assertTrue("Should contain print statement", compiler.getSourceCode().contains("output.append(convertNullValueToEmptyString(1));"));
    }
}
