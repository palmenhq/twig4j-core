package org.twig;

import org.junit.Assert;
import org.junit.Test;
import org.twig.compiler.ClassCompiler;
import org.twig.compiler.RuntimeTemplateCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigException;
import org.twig.exception.TwigRuntimeException;
import org.twig.loader.HashMapLoader;
import org.twig.loader.Loader;
import org.twig.syntax.Lexer;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.Module;
import org.twig.syntax.parser.node.type.Body;
import org.twig.template.Context;
import org.twig.template.Template;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.*;

public class EnvironmentTests {
    @Test
    public void testGetTemplateClass() throws LoaderException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo", "bar");
        Loader loader = new HashMapLoader(templates);
        Environment environment = new Environment(loader);

        // Just run this and make sure it doesn't throw any errors
        String className = environment.getTemplateClass("foo");
        // Assume template name = sha 256 of "bar"
        Assert.assertEquals(
                "Class name should be SHA256 of passed template name content + underscore 0",
                "Template_fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9_0",
                className
        );

        String classNameWithDefinedIndex = environment.getTemplateClass("foo", 1);
        Assert.assertEquals(
                "Class name should be SHA256 of passed template name content + underscore index",
                "Template_fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9_1",
                classNameWithDefinedIndex
        );
    }

    @Test
    public void testCanCompileSource() throws TwigException {
        Loader loaderStub = mock(HashMapLoader.class);
        Environment environment = new Environment(loaderStub);
        Lexer lexerStub = mock(Lexer.class);
        Parser parserStub = mock(Parser.class);
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        TokenStream tokenStream = new TokenStream();
        Module module = new Module(new Body(1));

        environment
                .setLexer(lexerStub)
                .setParser(parserStub)
                .setClassCompiler(compilerStub);

        when(loaderStub.getSource("foo")).thenReturn("bar");
        when(loaderStub.getCacheKey("foo")).thenReturn("templateCacheKey");
        when(lexerStub.tokenize("bar", "foo")).thenReturn(tokenStream);
        when(parserStub.parse(tokenStream)).thenReturn(module);
        when(compilerStub.compile(module)).thenReturn(compilerStub);
        when(compilerStub.getSourceCode()).thenReturn("compiled");

        Assert.assertEquals("Returned \"source code\" should be what compiler returned", "compiled", environment.compileSource("bar", "foo"));

        verify(lexerStub).tokenize("bar", "foo");
        verify(parserStub).parse(tokenStream);
        verify(compilerStub).compile(module);
        verify(compilerStub).getSourceCode();
    }

    @Test
    public void testCanLoadTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo", "bar");
        Environment environment = new Environment(new HashMapLoader(templates));

        // Just test so this method doesn't throw any errors
        Template loadedTemplate = environment.loadTemplate("foo");
    }

    @Test
    public void testCanRenderTemplate() throws TwigException {
        HashMap<String, String> templates = new HashMap<>();
        templates.put("foo", "bar");
        Environment environment = new Environment(new HashMapLoader(templates));

        String result = environment.render("foo");

        Assert.assertEquals("Rendered result should be template contents", "bar", result);
    }

    public void testCanResolveTemplate() throws TwigException {
        Loader loaderStub = mock(HashMapLoader.class);
        Environment environment = new Environment(loaderStub);

        Lexer lexerStub = mock(Lexer.class);
        Parser parserStub = mock(Parser.class);
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        RuntimeTemplateCompiler runtimeTemplateCompiler = mock(RuntimeTemplateCompiler.class);

        environment
                .setLexer(lexerStub)
                .setParser(parserStub)
                .setClassCompiler(compilerStub)
                .setRuntimeTemplateCompiler(runtimeTemplateCompiler);

        when(loaderStub.getSource("foo.twig")).thenReturn("foo");

        Template testTemplate = new Template_Test_0();
        when(runtimeTemplateCompiler.compile(anyString(), "foo.twig")).thenReturn(testTemplate);

        Assert.assertSame(
                "Template returned from runtime compiler should be template resolved",
                testTemplate,
                environment.resolveTemplate("foo.twig")
        );
    }

    public void testCanResolveTemplateFromList() throws TwigException {
        Loader loaderStub = mock(HashMapLoader.class);
        Environment environment = new Environment(loaderStub);

        Lexer lexerStub = mock(Lexer.class);
        Parser parserStub = mock(Parser.class);
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        RuntimeTemplateCompiler runtimeTemplateCompiler = mock(RuntimeTemplateCompiler.class);

        environment
                .setLexer(lexerStub)
                .setParser(parserStub)
                .setClassCompiler(compilerStub)
                .setRuntimeTemplateCompiler(runtimeTemplateCompiler);

        // First load attempt
        when(loaderStub.getSource("foo.twig")).thenThrow(LoaderException.notDefined("foo.twig"));
        // 2nd load attempt
        when(loaderStub.getSource("bar.twig")).thenReturn("bar");
        Template testTemplate = new Template_Test_0();
        when(runtimeTemplateCompiler.compile(anyString(), "bar.twig")).thenReturn(testTemplate);

        Assert.assertSame(
                "Template returned from runtime compiler should be template resolved",
                testTemplate,
                environment.resolveTemplate(Arrays.asList("foo.twig", "bar.twig"))
        );
    }

    protected class Template_Test_0 extends Template {
        @Override
        protected String doRender(Context context) throws TwigRuntimeException {
            return "foobar";
        }

        @Override
        public String getTemplateName() {
            return "foo.twig";
        }
    }
}
