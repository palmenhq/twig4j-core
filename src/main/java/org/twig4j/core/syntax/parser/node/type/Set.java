package org.twig4j.core.syntax.parser.node.type;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

import java.util.List;

public class Set extends Node {
    /**
     * Constructor
     * @param names The variable names
     * @param values The variable values
     * @param capture Not sure what this does - does nothing for now
     * @param line The line the node is on
     * @param tag Always pass "set" to this one
     */
    public Set(List<String> names, Node values, Boolean capture, Integer line, String tag) {
        super(line);
        setTag(tag);
        nodes = values.getNodes();
        putAttribute("names", names);
        putAttribute("capture", capture);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        List<String> names = (List<String>)getAttribute("names");

        compiler.addDebugInfo(this);

        for (Integer variableIndex = 0; variableIndex < names.size(); variableIndex ++) {
            compiler
                .write("((java.util.Map<String, Object>)context).put(")
                .writeString(names.get(variableIndex))
                .writeRaw(", ")
                .subCompile(nodes.get(variableIndex))
                .writeRaw(");\n");
        }

    }
}
