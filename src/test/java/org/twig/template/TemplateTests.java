package org.twig.template;

import org.junit.Assert;
import org.junit.Test;
import org.twig.Environment;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;

import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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

    @Test
    public void canGetObjectProperty() throws TwigException {

    }

    @Test
    public void canCompareDifferentConstants() throws TwigException {
        Template template = new TestCompareDifferentConstantsTemplate(new Environment());

        Assert.assertEquals(
                "Method compare should return false when comparing 2 different constants",
                "false",
                template.render()
        );
    }

    @Test
    public void canCompareEqualConstants() throws TwigException {
        Template template = new TestCompareSameConstantsTemplate(new Environment());

        Assert.assertEquals(
                "Method compare should return true when comparing 2 equal constants",
                "true",
                template.render()
        );
    }

    @Test(expected = TwigRuntimeException.class)
    public void throwsExceptionWhenComparingDifferentTypes() throws TwigException {
        Environment environment = new Environment();
        environment.enableStrictTypes();
        Template template = new TestCompareDifferentTypesConstantsTemplate(environment);

        template.render();
    }

    @Test
    public void failsSilentlyWhenComparingDifferentTypesWithStrictTypesDisabled() throws TwigException {
        Environment environment = new Environment();
        environment.disableStrictTypes();
        Template template = new TestCompareDifferentTypesConstantsTemplate(environment);

        Assert.assertEquals(
                "Method compare should return false when comparing constants of different types",
                "false",
                template.render()
        );
    }

    @Test
    public void canGetPropertyGetterIserHaser() throws TwigException {
        Map<String, TestClassWithGettersAndProperty> ctx = new HashMap<>();
        ctx.put("foo", new TestClassWithGettersAndProperty());
        Environment env = new Environment();

        env.enableStrictVariables();

        Template fooProperty = new TestGetFooPropertyTemplate(env);
        Assert.assertEquals("foo property should be fetched", "foo contents", fooProperty.render(ctx));

        Template barGetter = new TestGetBarGetterTemplate(env);
        Assert.assertEquals("bar getter should be called", "bar contents", barGetter.render(ctx));

        Template bazIser = new TestGetBazIserTemplate(env);
        Assert.assertEquals("baz iser should be called", "true", bazIser.render(ctx));

        Template quxHaser = new TestGetQuxHaserTemplate(env);
        Assert.assertEquals("qux haser should be called", "false", quxHaser.render(ctx));

        Template quuxMethod = new TestGetQuuxMethodTemplate(env);
        Assert.assertEquals("quux should be called", "quux contents", quuxMethod.render(ctx));
    }

    protected class TestStringTemplate extends Template {
        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return "foo";
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestVariableTemplate extends Template {
        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return getContext(context, "foo", false, 1).toString();
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestMethodCallTemplate extends Template {
        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
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

    protected class TestCompareDifferentConstantsTemplate extends Template {
        public TestCompareDifferentConstantsTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(compare("foo", "bar"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestCompareSameConstantsTemplate extends Template {
        public TestCompareSameConstantsTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(compare("foo", "foo"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestCompareDifferentTypesConstantsTemplate extends Template {
        public TestCompareDifferentTypesConstantsTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(compare("true", true));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestGetFooPropertyTemplate extends Template {
        public TestGetFooPropertyTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(getAttribute(getContext(context, "foo", false, 1), "foo", Arrays.asList(), "any"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestGetBarGetterTemplate extends Template {
        public TestGetBarGetterTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(getAttribute(getContext(context, "foo", false, 1), "bar", Arrays.asList(), "any"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestGetBazIserTemplate extends Template {
        public TestGetBazIserTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(getAttribute(getContext(context, "foo", false, 1), "baz", Arrays.asList(), "any"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestGetQuxHaserTemplate extends Template {
        public TestGetQuxHaserTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(getAttribute(getContext(context, "foo", false, 1), "qux", Arrays.asList(), "any"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestGetQuuxMethodTemplate extends Template {
        public TestGetQuuxMethodTemplate(Environment environment) {
            super(environment);
        }

        @Override
        protected String doRender(Map<String, ?> context) throws TwigRuntimeException {
            return String.valueOf(getAttribute(getContext(context, "foo", false, 1), "quux", Arrays.asList(), "any"));
        }

        @Override
        public String getTemplateName() {
            return "foo";
        }
    }

    protected class TestClassWithGettersAndProperty {
        public String foo = "foo contents";

        public String getBar() {
            return "bar contents";
        }

        public boolean isBaz() {
            return true;
        }

        public boolean hasQux() {
            return false;
        }

        public String quux() {
            return "quux contents";
        }
    }
}
