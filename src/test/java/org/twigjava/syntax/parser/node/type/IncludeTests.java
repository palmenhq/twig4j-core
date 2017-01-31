package org.twigjava.syntax.parser.node.type;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.type.expression.StringConstant;

public class IncludeTests {
    @Test
    public void canCompile() throws SyntaxErrorException, LoaderException, TwigRuntimeException {
        Environment environment = new Environment();
        ClassCompiler compiler = new ClassCompiler(environment);

        Include include = new Include(new StringConstant("bar.twigjava", 1), null, false, false, 1, "include");

        include.compile(compiler);

        Assert.assertTrue("Should compile some form of source code", compiler.getSourceCode().length() > 1);
    }
}
