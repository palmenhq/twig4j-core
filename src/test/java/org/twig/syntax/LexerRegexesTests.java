package org.twig.syntax;

import org.junit.Assert;
import org.junit.Test;
import org.twig.syntax.operator.BinaryAdd;
import org.twig.syntax.operator.Operator;

import java.util.HashMap;

public class LexerRegexesTests {
    @Test
    public void canCreateOperators() {
        HashMap<String, Operator> binaryOperators = new HashMap<>();
        binaryOperators.put("+", new BinaryAdd());

        LexerRegexes regexes = new LexerRegexes(new LexerOptions(), new HashMap<>(), binaryOperators);

        Assert.assertEquals("Correct regex should be returned", "^(\\Q+\\E)", regexes.getOperators().toString());
    }
}
