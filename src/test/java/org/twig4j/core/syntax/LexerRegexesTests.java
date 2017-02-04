package org.twig4j.core.syntax;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.syntax.operator.BinaryAdd;
import org.twig4j.core.syntax.operator.BinaryIn;
import org.twig4j.core.syntax.operator.BinaryNotIn;
import org.twig4j.core.syntax.operator.Operator;

import java.util.HashMap;

public class LexerRegexesTests {
    @Test
    public void canCreateOperators() {
        HashMap<String, Operator> binaryOperators = new HashMap<>();
        binaryOperators.put("+", new BinaryAdd());

        LexerRegexes regexes = new LexerRegexes(new LexerOptions(), new HashMap<>(), binaryOperators);

        Assert.assertEquals("Correct regex should be returned", "^(\\Q+\\E|=)", regexes.getOperators().toString());
    }

    @Test
    public void canCreateOperatorsThatEndWithCharacter() {
        HashMap<String, Operator> binaryOperators = new HashMap<>();
        binaryOperators.put("in", new BinaryIn());

        LexerRegexes regexes = new LexerRegexes(new LexerOptions(), new HashMap<>(), binaryOperators);

        Assert.assertEquals("Correct regex should be returned", "^(\\Qin\\E(?=\\s+)|=)", regexes.getOperators().toString());
    }

    @Test
    public void sortsOperatorsCorrectly() {
        HashMap<String, Operator> binaryOperators = new HashMap<>();
        binaryOperators.put("in", new BinaryIn());
        binaryOperators.put("not in", new BinaryNotIn());

        LexerRegexes regexes = new LexerRegexes(new LexerOptions(), new HashMap<>(), binaryOperators);

        Assert.assertEquals("Correct regex should be returned", "^(\\Qnot in\\E(?=\\s+)|\\Qin\\E(?=\\s+)|=)", regexes.getOperators().toString());
    }
}
