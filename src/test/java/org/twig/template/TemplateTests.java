package org.twig.template;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;

public class TemplateTests {
    @Test
    public void testCanRenderString() throws TwigRuntimeException {
        Template template = new TestStringTemplate();
        Assert.assertEquals("Rendered string should be returned", "foo", template.render());
    }

    @Test
    public void testCanRenderVariable() throws TwigRuntimeException {
        HashMap<String, String> context = new HashMap<>();
        context.put("foo", "bar");
        Template template = new TestVariableTemplate();

        Assert.assertEquals("Rendered variable contents should be returned", "bar", template.render(context));
    }

    @Test(expected = TwigRuntimeException.class)
    public void testAccessUndefinedVariableThrowsRuntimeException() throws TwigRuntimeException {
        Template template = new TestVariableTemplate();
        Environment environment = mock(Environment.class);

        when(environment.isStrictVariables()).thenReturn(true);

        template.setEnvironment(environment);

        template.render(new HashMap<>());
    }

    @Test
    public void canCallMethodOnContextObject() throws TwigException {
        HashMap<String, Object> context = new HashMap<>();
        context.put("foo", new ClassWithBarMethod());
        Template template = new TestMethodCallTemplate();

        Assert.assertEquals(
                "Method contents should be returned",
                "some argument",
                template.render(context)
        );
    }

    protected class TestStringTemplate extends Template {
        @Override
        protected String doRender(HashMap<String, ?> context) throws TwigRuntimeException {
            return "foo";
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestVariableTemplate extends Template {
        @Override
        protected String doRender(HashMap<String, ?> context) throws TwigRuntimeException {
            return getContext(context, "foo", false, 1).toString();
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestMethodCallTemplate extends Template {
        @Override
        protected String doRender(HashMap<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(getAttribute(context.get("foo"), "bar", Arrays.asList("some ", "argument"), "method"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class ClassWithBarMethod {
        public String bar(String anArgument, String anotherArgument) {
            return anArgument + anotherArgument;
        }
    }
}
