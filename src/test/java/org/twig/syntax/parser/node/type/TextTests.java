package org.twig.syntax.parser.node.type;

import org.junit.Test;
import org.twig.compiler.ClassCompiler;

import java.util.ArrayList;
import java.util.HashMap;

import static org.mockito.Mockito.*;

public class TextTests {
    @Test
    public void canCompile() {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("data", "foo");
        Text text = new Text(new ArrayList<>(), data, 1, "");
        text.compile(compilerStub);
        verify(compilerStub).writeLine("output = output.concat(\"foo\");");
    }

    @Test
    public void canCompileWithQuotes() {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("data", "foo \"bar\"");
        Text text = new Text(new ArrayList<>(), data, 1, "");
        text.compile(compilerStub);

        verify(compilerStub).writeLine("output = output.concat(\"foo \\\"bar\\\"\");");
    }

    @Test
    public void canCompileWithBackslashAndQuotes() {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("data", "foo \\\"bar\"");
        Text text = new Text(new ArrayList<>(), data, 1, "");
        text.compile(compilerStub);

        verify(compilerStub).writeLine("output = output.concat(\"foo \\\\\\\"bar\\\"\");");
    }

    @Test
    public void canCompileWithNewline() {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        HashMap<String, String> data = new HashMap<String, String>();
        data.put("data", "foo\nbar");
        Text text = new Text(new ArrayList<>(), data, 1, "");
        text.compile(compilerStub);

        verify(compilerStub).writeLine("output = output.concat(\"foo\\nbar\");");
    }
}
