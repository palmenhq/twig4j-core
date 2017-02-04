package org.twig4j.core.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twig4j.core.exception.Twig4jException;

import java.util.HashMap;

public class RenderMathsTests extends FunctionalTests {
    @Test
    public void canRenderNumber() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twig4j", "{{ 1134.12341 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1134.12341 should be rendered", "1134.12341", environment.render("foo.twig4j"));
    }

    @Test
    public void canDoAddition() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("addition.twig4j", "{{ 1 + 1 }}");
        templates.put("multiple-addition.twig4j", "{{ 1 + 2+3+4 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 + 1 should be 2", "2", environment.render("addition.twig4j"));
        Assert.assertEquals("1 + 2 + 3 + 4 should be 10", "10", environment.render("multiple-addition.twig4j"));
    }

    @Test
    public void canDoSubtraction() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("subtraction.twig4j", "{{ 2 - 1 }}");
        templates.put("multiple-subtraction.twig4j", "{{ 4 - 3 - 2-1 }}");
        setupEnvironment(templates);

        Assert.assertEquals("2 - 1 should be 1", "1", environment.render("subtraction.twig4j"));
        Assert.assertEquals("4 - 3 - 2 - 1 should be -2", "-2", environment.render("multiple-subtraction.twig4j"));
    }

    @Test
    public void canDoMultiplication() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("multiplication.twig4j", "{{ 2 * 5}}");
        setupEnvironment(templates);

        Assert.assertEquals("2 * 5 should be 10", "10", environment.render("multiplication.twig4j"));
    }

    @Test
    public void canDoOddDivision() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("division.twig4j", "{{ 5 / 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals("5/2 should be 2.5", "2.5", environment.render("division.twig4j"));
    }

    @Test
    public void canDoEvenDivision() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("division.twig4j", "{{ 10 / 2 }}");
        setupEnvironment(templates);
        String result = environment.render("division.twig4j");

        Assert.assertEquals("10/2 should be 5.0", "5.0", result);

    }

    @Test
    public void canDoAdditionAndSubtractionAndMultiplication() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("maths.twig4j", "{{ 1 + 2 * 5 - 2 / 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(1 + 2 * 5 - 2 / 2) should be 10.0 (since we're doing division)",
                "10.0",
                environment.render("maths.twig4j")
        );
    }

    @Test
    public void canDoMathsWithParenthesises() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("maths.twig4j", "{{ (1 + 2) * (5 - 2) / 3 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "((1 + 2) * (5 - 2) / 3) should be 3.0",
                "3.0",
                environment.render("maths.twig4j")
        );
    }

    @Test
    public void canDoFloor() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("floor.twig4j", "{{ 3 // 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(3 // 2) should be 1",
                "1",
                environment.render("floor.twig4j")
        );
    }

    @Test
    public void canDoModulo() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("mod.twig4j", "{{ 3 % 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals(
                "(3 % 2) should be 1",
                "1",
                environment.render("mod.twig4j")
        );
    }

    @Test
    public void canDoPowerOf() throws Twig4jException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("powerOf.twig4j", "{{ 3 ** 3 }}");
        setupEnvironment(templates);

        Assert.assertEquals("(3 ** 3) should be 27.0", "27.0", environment.render("powerOf.twig4j"));
    }
}
