package org.twig4j.core.syntax.parser.node.type.expression;

import org.twig4j.core.compiler.ClassCompiler;
import org.twig4j.core.exception.LoaderException;
import org.twig4j.core.exception.Twig4jRuntimeException;
import org.twig4j.core.syntax.parser.node.Node;

public class Filter extends Call {
    public Filter(Node node, Constant filterName, Node arguments, Integer line, String tag) {
        super(line);
        setTag(tag);

        addNode(node); // 0 = Body node
        addNode(filterName); // 1 = filter name
        addNode(arguments); // 2 = arguments
    }

    public void compile(ClassCompiler compiler) throws LoaderException, Twig4jRuntimeException {
        org.twig4j.core.filter.Filter filter = compiler.getEnvironment().getFilter(((String)getNode(1).getAttribute("data")));
        putAttribute("name", filter.getName());
        putAttribute("type", "filter");
        putAttribute("needs_environment", filter.getOptions().needsEnvironment());
        putAttribute("needs_context", filter.getOptions().needsContext());
        putAttribute("arguments", filter.getArguments());
        putAttribute("callable", filter.getMethod());
        putAttribute("is_variadic", filter.getOptions().isVariadic());

        compileCallable(compiler);
    }
}
