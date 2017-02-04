package org.twig4j.core.syntax.parser.node.type.expression;

import org.junit.Assert;
import org.junit.ComparisonFailure;
import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigRuntimeException;

public class HashTests {
    @Test
    public void canCompile() throws LoaderException, TwigRuntimeException {
        ClassCompiler compiler = new ClassCompiler(new Environment());

        Hash hash = new Hash(1);
        hash.putAttribute("foo", new Constant("Foo", 1));
        hash.putAttribute("bar", new Constant("Bar", 1));

        hash.compile(compiler);

        // Since it's a hashmap we're not sure of the order of the attributes so just try both
        try {
            Assert.assertEquals(
                    "Compiled source code should be valid java array",
                    "((new org.twig4j.core.util.HashMap()).put(\"foo\", \"Foo\").put(\"bar\", \"Bar\"))",
                    compiler.getSourceCode()
            );
        } catch (ComparisonFailure e) {
            Assert.assertEquals(
                    "Compiled source code should be valid java array",
                    "((new org.twig4j.core.util.HashMap()).put(\"bar\", \"Bar\").put(\"foo\", \"Foo\"))",
                    compiler.getSourceCode()
            );
        }
    }
}
