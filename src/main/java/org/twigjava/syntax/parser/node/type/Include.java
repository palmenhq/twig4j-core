package org.twigjava.syntax.parser.node.type;

import org.twigjava.compiler.ClassCompiler;
import org.twigjava.exception.LoaderException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.Output;

import java.util.Arrays;

public class Include extends Node implements Output {
    /**
     * Constructor
     *
     * @param template      The template file name to load (usually a string constant)
     * @param variables     Variables to pass to the template
     * @param only          Whether to "sandbox" the template so it doesn't access this template's context
     * @param ignoreMissing Ignore if the template is missing and fail silently?
     * @param lineNumber    The line which this tag is on
     * @param tag           Always pass "include"
     */
    public Include(Node template, Node variables, Boolean only, Boolean ignoreMissing, Integer lineNumber, String tag) {
        super(lineNumber);
        setTag(tag);
        nodes = Arrays.asList(template, variables);
        putAttribute("only", only);
        putAttribute("ignore_missing", ignoreMissing);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.addDebugInfo(this);

        if ((Boolean) getAttribute("ignore_missing")) {
            compiler
                .writeLine("try {")
                .indent();
        }


        if ((Boolean)getAttribute("only")) {
            compiler
                .writeLine("context.put(\"_include_context\", new org.twigjava.template.Context());");
        } else {
            compiler.writeLine("context.put(\"_include_context\", context.clone());");
        }

        if (getNode(1) != null) {
            compiler
                .write("((org.twigjava.template.Context)context.get(\"_include_context\")).putAll(")
                .subCompile(getNode(1)) // Variables
                .writeRaw(");\n");
        }


        compiler.write("output = output.concat(String.valueOf(");

        // Load the template
        compiler
            .writeRaw("loadTemplate(")
            .subCompile(getNode(0)) // expr/template name
            .writeRaw(", ")
            .representValue(null)
            .writeRaw(", ")
            .representValue(getLine())
            .writeRaw(", ")
            .representValue(null)
            .writeRaw(")");

        compiler
            .writeRaw(".render(")
            .writeRaw("((org.twigjava.template.Context)context.get(\"_include_context\"))")
            .writeRaw(")");

        compiler.writeRaw("));\n");

        compiler.writeLine("context.remove(\"_include_context\");");

        if ((Boolean) getAttribute("ignore_missing")) {
            compiler
                .unIndent()
                .writeLine("} catch (TwigException e) {")
                .indent()
                    .writeLine("// Ignore missing")
                .unIndent()
                .writeLine("}");
        }
    }
}
