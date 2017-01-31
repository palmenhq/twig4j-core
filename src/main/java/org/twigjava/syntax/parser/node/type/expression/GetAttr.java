package org.twigjava.syntax.parser.node.type.expression;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;

/**
 * Get an attribute on an object in the context. Can be a "regular" method call (`object.method()`), direct access to a
 * property (`object.property`)
 * or reference to a getter (`object.property` will be called as `object.getProperty()`),
 * "isser" (`object.property` will then be called as `object.isProperty()`) or "hasser" (`object.property` will lastly be
 * attempted to be called as `object.hasProperty()`).
 *
 * @see org.twigjava.template.Template#getAttribute
 */
public class GetAttr extends Expression {
    public GetAttr(Integer line) {
        super(line);
    }

    public GetAttr(Expression node, Expression attribute, Expression arguments, String type, Integer line) {
        super(line);
        addNode(0, node);
        addNode(1, attribute);
        addNode(2, arguments);

        putAttribute("type", type);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
                .writeRaw("getAttribute(")
                .subCompile(getNode(0)) // The "name" node
                .writeRaw(", ")
                .subCompile(getNode(1)) // The "item" (attribute)
                .writeRaw(", ")
                .subCompile(getNode(2)) // The arguments
                .writeRaw(", ")
                .representValue(String.valueOf(getAttribute("type"))) // ie "method"
                .writeRaw(")");
    }
}
