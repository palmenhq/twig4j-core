package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.Twig4jException;

import java.util.HashMap;

public class RenderMathsTests extends FunctionalTests {
    @Test
    public void canRenderNumber() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig", "{{ 1134.12341 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1134.12341 should be rendered", "1134.12341", environment.render("foo.twig"));
    }

    @Test
    public void canDoAddition() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("addition.twig", "{{ 1 + 1 }}");
        templates.put("multiple-addition.twig", "{{ 1 + 2+3+4 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 + 1 should be 2", "2", environment.render("addition.twig"));
        Assert.assertEquals("1 + 2 + 3 + 4 should be 10", "10", environment.render("multiple-addition.twig"));
    }

    @Test
    public void canDoSubtraction() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("subtraction.twig", "{{ 2 - 1 }}");
        templates.put("multiple-subtraction.twig", "{{ 4 - 3 - 2-1 }}");
        setupEnvironment(templates);

        Assert.assertEquals("2 - 1 should be 1", "1", environment.render("subtraction.twig"));
        Assert.assertEquals("4 - 3 - 2 - 1 should be -2", "-2", environment.render("multiple-subtraction.twig"));
    }

    @Test
    public void canDoMultiplication() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("multiplication.twig", "{{ 2 * 5}}");
        setupEnvironment(templates);

        Assert.assertEquals("2 * 5 should be 10", "10", environment.render("multiplication.twig"));
    }

    @Test
    public void canDoOddDivision() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("division.twig", "{{ 5 / 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals("5/2 should be 2.5", "2.5", environment.render("division.twig"));
    }

    @Test
    public void canDoEvenDivision() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("division.twig", "{{ 10 / 2 }}");
        setupEnvironment(templates);
        String result = environment.render("division.twig");

        Assert.assertEquals("10/2 should be 5", "5", result);

    }

    @Test
    public void canDoAdditionAndSubtractionAndMultiplication() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("maths.twig", "{{ 1 + 2 * 5 - 2 / 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(1 + 2 * 5 - 2 / 2) should be 10",
                "10",
                environment.render("maths.twig")
        );
    }

    @Test
    public void canDoMathsWithParenthesises() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("maths.twig", "{{ (1 + 2) * (5 - 2) / 3 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "((1 + 2) * (5 - 2) / 3) should be 3",
                "3",
                environment.render("maths.twig")
        );
    }

    @Test
    public void canDoFloor() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("floor.twig", "{{ 3 // 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(3 // 2) should be 1",
                "1",
                environment.render("floor.twig")
        );
    }

    @Test
    public void canDoModulo() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("mod.twig", "{{ 3 % 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(3 % 2) should be 1",
                "1",
                environment.render("mod.twig")
        );
    }

    @Test
    public void canDoPowerOf() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("powerOf.twig", "{{ 3 ** 3 }}");
        setupEnvironment(templates);

        Assert.assertEquals("(3 ** 3) should be 27", "27", environment.render("powerOf.twig"));
    }
}
