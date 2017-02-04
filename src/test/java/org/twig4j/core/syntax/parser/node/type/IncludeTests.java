package org.twig4j.core.syntax.parser.node.type;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.SyntaxErrorException;
import org.twig4j.core.exception.TwigRuntimeException;
import org.twig4j.core.syntax.parser.node.type.expression.StringConstant;

public class IncludeTests {
    @Test
    public void canCompile() throws SyntaxErrorException, LoaderException, TwigRuntimeException {
        Environment environment = new Environment();
        ClassCompiler compiler = new ClassCompiler(environment);

        Include include = new Include(new StringConstant("bar.twig4j", 1), null, false, false, 1, "include");

        include.compile(compiler);

        Assert.assertTrue("Should compile some form of source code", compiler.getSourceCode().length() > 1);
    }
}
