package org.twigjava.functional;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.exception.TwigException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.template.Context;

import java.util.HashMap;

public class RenderLogicTests extends FunctionalTests {
    @Test
    public void canRenderPlainBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ true }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twigjava"));
    }

    @Test
    public void canRenderPlainInverseBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ not true }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render inversed text bool", "false", environment.render("foo.twigjava"));
    }

    @Test
    public void canRenderComparedBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ true == false }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "false", environment.render("foo.twigjava"));
    }

    @Test
    public void canCompareBoolVars() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ foo == false }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", false);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twigjava", ctx));
    }

    @Test
    public void canCompareStringVars() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ foo == \"bar\" }}");
        setupEnvironment(templates);

        Context ctx = new Context();
        ctx.put("foo", "bar");

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twigjava", ctx));
    }

    @Test
    public void canDoStartsWith() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ 'foobar' starts with 'foo' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Starts with should be true if string starts with foo", "true", environment.render("foo.twigjava"));
    }

    @Test
    public void canDoEndsWith() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ 'foobar' ends with 'bar' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Starts with should be true if string ends with bar", "true", environment.render("foo.twigjava"));
    }

    @Test
    public void matches() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("caseSensitive.twigjava", "{{ 'Foobar' matches '/^[a-z]+/' }}");
        templates.put("caseInsensitive.twigjava", "{{ 'Foobar' matches '/^[a-z]+/i' }}");
        setupEnvironment(templates);

        Assert.assertEquals("Case sensitive regex shouldn't match string with wrong case", "false", environment.render("caseSensitive.twigjava"));
        Assert.assertEquals("Case sensitive regex should match string with wrong case", "true", environment.render("caseInsensitive.twigjava"));
    }

    @Test
    public void canDoIn() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("trueString.twigjava", "{{ 'foo' in 'foobar' }}");
        templates.put("trueArray.twigjava", "{{ 'foo' in ['foo', 'bar'] }}");
        templates.put("falseString.twigjava", "{{ 'nope' in 'foobar' }}");
        templates.put("falseArray.twigjava", "{{ 'nope' in ['foo', 'bar'] }}");
        templates.put("falseInverseString.twigjava", "{{ 'foo' not in 'foobar' }}");
        templates.put("trueInverseArray.twigjava", "{{ 'nope' not in ['foo', 'bar'] }}");
        setupEnvironment(templates);

        Assert.assertEquals("Can find if string is present in string", "true", environment.render("trueString.twigjava"));
        Assert.assertEquals("Can find if string is present in array", "true", environment.render("trueArray.twigjava"));
        Assert.assertEquals("Can't find if string is not present in string", "false", environment.render("falseString.twigjava"));
        Assert.assertEquals("Can't find if string is not present in array", "false", environment.render("falseArray.twigjava"));
        Assert.assertEquals("Can find if string is not present in string when actually is", "false", environment.render("falseInverseString.twigjava"));
        Assert.assertEquals("Can find if string is not present in array when is not", "true", environment.render("trueInverseArray.twigjava"));
    }

    @Test(expected = TwigRuntimeException.class)
    public void cantCompareDifferentTypesWithStrictTypesEnabled() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ foo == 1 }}");
        setupEnvironment(templates);
        environment.enableStrictTypes();

        Context ctx = new Context();
        ctx.put("foo", "1");

        environment.render("foo.twigjava", ctx);
    }

    @Test
    public void canRenderNotComparedBoolean() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ true != false }}");
        setupEnvironment(templates);

        Assert.assertEquals("Bool should render text bool", "true", environment.render("foo.twigjava"));
    }

    @Test
    public void canDoOr() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ false or true }}");
        setupEnvironment(templates);

        Assert.assertEquals("\"false or true\" should be true", "true", environment.render("foo.twigjava"));
    }

    @Test
    public void canDoAnd() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo.twigjava", "{{ false and true }}");
        setupEnvironment(templates);

        Assert.assertEquals("\"false and true\" should be false", "false", environment.render("foo.twigjava"));
    }

    @Test
    public void canDoLessThan() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("true.twigjava", "{{ 1 < 2 }}");
        templates.put("false.twigjava", "{{ 2 < 1 }}");
        setupEnvironment(templates);

        Assert.assertEquals("2 < 1 should be false", "false", environment.render("false.twigjava"));
        Assert.assertEquals("1 < 2 should be true", "true", environment.render("true.twigjava"));
    }

    @Test
    public void canDoGreaterThan() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("true.twigjava", "{{ 2 > 1 }}");
        templates.put("false.twigjava", "{{ 1 > 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals("2 > 1 should be true", "true", environment.render("true.twigjava"));
        Assert.assertEquals("1 > 2 should be false", "false", environment.render("false.twigjava"));
    }

    @Test
    public void canDoGreaterThanOrEqual() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("true.twigjava", "{{ 1 >= 1 }}");
        templates.put("false.twigjava", "{{ 1 >= 2 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 >= 1 should be true", "true", environment.render("true.twigjava"));
        Assert.assertEquals("1 >= 2 should be false", "false", environment.render("false.twigjava"));
    }

    @Test
    public void canDoLessThanOrEqual() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("true.twigjava", "{{ 1 <= 1 }}");
        templates.put("false.twigjava", "{{ 2 <= 1 }}");
        setupEnvironment(templates);

        Assert.assertEquals("1 <= 1 should be true", "true", environment.render("true.twigjava"));
        Assert.assertEquals("2 <= 1 should be false", "false", environment.render("false.twigjava"));
    }
}
