package org.twig4j.core.syntax.parser.node;

import org.junit.Test;
import org.twig4j.core.Environment;
import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.TwigRuntimeException;

import static org.mockito.Mockito.*;

public class NodeTests {
    @Test
    public void canCompileChildNodes() throws LoaderException, TwigRuntimeException {
        Node node = new Node(1);
        Node childNode1 = mock(Node.class);
        Node childNode2 = mock(Node.class);

        node.addNode(childNode1);
        node.addNode(childNode2);

        ClassCompiler compiler = new ClassCompiler(new Environment());

        node.compile(compiler);

        verify(childNode1).compile(compiler);
        verify(childNode2).compile(compiler);
    }
}
