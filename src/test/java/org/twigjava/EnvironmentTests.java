package org.twigjava;

import org.junit.Assert;
import org.junit.Test;
import org.twigjava.compiler.ClassCompiler;
import org.twigjava.compiler.RuntimeTemplateCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.loader.HashMapLoader;
import org.twigjava.loader.Loader;
import org.twigjava.syntax.Lexer;
import org.twigjava.syntax.TokenStream;
import org.twigjava.syntax.parser.Parser;
import org.twigjava.syntax.parser.node.Module;
import org.twigjava.syntax.parser.node.type.Body;
import org.twigjava.template.Context;
import org.twigjava.template.Template;

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

        when(loaderStub.getSource("foo.twigjava")).thenReturn("foo");

        Template testTemplate = new Template_Test_0();
        when(runtimeTemplateCompiler.compile(anyString(), "foo.twigjava")).thenReturn(testTemplate);

        Assert.assertSame(
                "Template returned from runtime compiler should be template resolved",
                testTemplate,
                environment.resolveTemplate("foo.twigjava")
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
        when(loaderStub.getSource("foo.twigjava")).thenThrow(LoaderException.notDefined("foo.twigjava"));
        // 2nd load attempt
        when(loaderStub.getSource("bar.twigjava")).thenReturn("bar");
        Template testTemplate = new Template_Test_0();
        when(runtimeTemplateCompiler.compile(anyString(), "bar.twigjava")).thenReturn(testTemplate);

        Assert.assertSame(
                "Template returned from runtime compiler should be template resolved",
                testTemplate,
                environment.resolveTemplate(Arrays.asList("foo.twigjava", "bar.twigjava"))
        );
    }

    protected class Template_Test_0 extends Template {
        @Override
        protected String doDisplay(Context context, Map<String, TemplateBlockMethodSet> blocks) throws TwigRuntimeException {
            return "foobar";
        }

        @Override
        public String getTemplateName() {
            return "foo.twigjava";
        }
    }
}
