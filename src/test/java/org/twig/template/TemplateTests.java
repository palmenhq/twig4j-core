package org.twig.template;

import org.junit.Assert;
import org.junit.Test;
import org.twig.exception.TwigRuntimeException;

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

        template.render(new HashMap<>());
    }

    protected class TestStringTemplate extends Template {
        @Override
        protected String doRender(HashMap<String, String> context) throws TwigRuntimeException {
            return "foo";
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestVariableTemplate extends Template {
        @Override
        protected String doRender(HashMap<String, String> context) throws TwigRuntimeException {
            return getContext(context, "foo", false, 1);
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }
}
