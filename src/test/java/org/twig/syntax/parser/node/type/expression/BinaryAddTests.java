package org.twig.syntax.parser.node.type.expression;

import org.junit.Test;
import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.syntax.parser.node.Node;

import static org.mockito.Mockito.*;

public class BinaryAddTests {
    @Test
    public void testCompile() throws LoaderException {
        ClassCompiler compilerStub = mock(ClassCompiler.class);
        Node left = new Node(1);
        Node right = new Node(2);
        BinaryAdd concatNode = new BinaryAdd(left, right, 1);

        when(compilerStub.writeRaw(anyString())).thenReturn(compilerStub);
        when(compilerStub.compile(anyObject())).thenReturn(compilerStub);

        concatNode.compile(compilerStub);

        verify(compilerStub).compile(left);
        verify(compilerStub).writeRaw(" + ");
        verify(compilerStub).compile(right);
    }
}
