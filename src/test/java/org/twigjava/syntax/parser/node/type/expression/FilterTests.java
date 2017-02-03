package org.twigjava.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.Environment;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.extension.CoreFilters;

import static org.mockito.Mockito.*;

public class FilterTests {
    @Test
    // {{ 'foo'|upper }}
    public void canCompile() throws LoaderException, TwigRuntimeException, NoSuchMethodException {
        Environment environment = mock(Environment.class);
        ClassCompiler compiler = new ClassCompiler(environment);

        StringConstant bodyNode = new StringConstant("foo", 1);
        StringConstant name = new StringConstant("upper", 1);
        Array arguments = new Array(1);
        Filter filterNode = new Filter(bodyNode, name, arguments, 1, null);

        when(environment.getFilter("upper")).thenReturn(new org.twigjava.filter.Filter("upper", CoreFilters.class.getMethod("upper", String.class)));

        filterNode.compile(compiler);

        // Just make sure the thing actually compiles and let the functional tests handle that the implementation works
        Assert.assertTrue(
                "Should compile some form of source code",
                compiler.getSourceCode().length() > 0
        );
    }
}