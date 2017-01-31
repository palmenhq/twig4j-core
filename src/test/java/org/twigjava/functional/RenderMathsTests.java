package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.TwigException;

import java.util.HashMap;

public class RenderMathsTests extends FunctionalTests {
    @Test
    public void canRenderNumber() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ 1134.12341 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1134.12341 should be rendered", "1134.12341", environment.render("foo.twigjava"));
    }

    @Test
    public void canDoAddition() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("addition.twigjava", "{{ 1 + 1 }}");
        templates.put("multiple-addition.twigjava", "{{ 1 + 2+3+4 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 + 1 should be 2", "2", environment.render("addition.twigjava"));
        Assert.assertEquals("1 + 2 + 3 + 4 should be 10", "10", environment.render("multiple-addition.twigjava"));
    }

    @Test
    public void canDoSubtraction() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("subtraction.twigjava", "{{ 2 - 1 }}");
        templates.put("multiple-subtraction.twigjava", "{{ 4 - 3 - 2-1 }}");
        setupEnvironment(templates);

        Assert.assertEquals("2 - 1 should be 1", "1", environment.render("subtraction.twigjava"));
        Assert.assertEquals("4 - 3 - 2 - 1 should be -2", "-2", environment.render("multiple-subtraction.twigjava"));
    }

    @Test
    public void canDoMultiplication() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("multiplication.twigjava", "{{ 2 * 5}}");
        setupEnvironment(templates);

        Assert.assertEquals("2 * 5 should be 10", "10", environment.render("multiplication.twigjava"));
    }

    @Test
    public void canDoOddDivision() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("division.twigjava", "{{ 5 / 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals("5/2 should be 2.5", "2.5", environment.render("division.twigjava"));
    }

    @Test
    public void canDoEvenDivision() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("division.twigjava", "{{ 10 / 2 }}");
        setupEnvironment(templates);
        String result = environment.render("division.twigjava");

        Assert.assertEquals("10/2 should be 5.0", "5.0", result);

    }

    @Test
    public void canDoAdditionAndSubtractionAndMultiplication() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("maths.twigjava", "{{ 1 + 2 * 5 - 2 / 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(1 + 2 * 5 - 2 / 2) should be 10.0 (since we're doing division)",
                "10.0",
                environment.render("maths.twigjava")
        );
    }

    @Test
    public void canDoMathsWithParenthesises() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("maths.twigjava", "{{ (1 + 2) * (5 - 2) / 3 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "((1 + 2) * (5 - 2) / 3) should be 3.0",
                "3.0",
                environment.render("maths.twigjava")
        );
    }

    @Test
    public void canDoFloor() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("floor.twigjava", "{{ 3 // 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(3 // 2) should be 1",
                "1",
                environment.render("floor.twigjava")
        );
    }

    @Test
    public void canDoModulo() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("mod.twigjava", "{{ 3 % 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(3 % 2) should be 1",
                "1",
                environment.render("mod.twigjava")
        );
    }

    @Test
    public void canDoPowerOf() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("powerOf.twigjava", "{{ 3 ** 3 }}");
        setupEnvironment(templates);

        Assert.assertEquals("(3 ** 3) should be 27.0", "27.0", environment.render("powerOf.twigjava"));
    }
}
