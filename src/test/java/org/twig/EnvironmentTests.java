package org.twig;

import org.junit.Assert;
import org.junit.Test;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigException;
import org.twig.loader.HashMapLoader;
import org.twig.loader.Loader;
import org.twig.syntax.Lexer;
import org.twig.syntax.TokenStream;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.Module;
import org.twig.syntax.parser.node.type.Body;
import org.twig.template.Template;

import java.util.HashMap;

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
                "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9_0",
                className
        );

        String classNameWithDefinedIndex = environment.getTemplateClass("foo", 1);
        Assert.assertEquals(
                "Class name should be SHA256 of passed template name content + underscore index",
                "fcde2b2edba56bf408601fb721fe9b5c338d10ee429ea04fae5511b68fbf8fb9_1",
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
}
