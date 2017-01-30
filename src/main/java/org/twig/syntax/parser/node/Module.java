package org.twig.syntax.parser.node;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.Compilable;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;

import java.util.List;

public class Module implements Compilable {
    Node bodyNode;
    Node parent;
    List<String> blocks;

    protected String fileName = "";

    public Module(Node bodyNode, Node parent) {
        this.bodyNode = bodyNode;
        this.parent = parent;
    }

    public Module(Node bodyNode, Node parent, String fileName) {
        this.bodyNode = bodyNode;
        this.parent = parent;
        this.fileName = fileName;
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        String className = compiler.getEnvironment().getTemplateClass(this.fileName);

        compileClassHeader(compiler, className);

        if (parent != null) {
            compileConstructor(compiler, className);
        }

        compileRender(compiler);

        compileClassFooter(compiler);
    }

    protected void compileClassHeader(ClassCompiler compiler, String className) throws LoaderException {
        String baseClass = compiler.getEnvironment().getTemplateBaseClass();

        compiler
                .writeLine("package " + compiler.getEnvironment().getTemplatePackage() + ";\n")
                .writeLine("import org.twig.exception.TwigException;\n")
                .writeLine("/**")
                .writeLine(" * ".concat(this.fileName))
                .writeLine(" */")
                .write("public class ".concat(className))
                .writeLine(" extends ".concat(baseClass).concat(" {"))
                .indent();
    }

    protected void compileConstructor(ClassCompiler compiler, String className) throws LoaderException, TwigRuntimeException {
        compiler
            .writeLine("public " + className + "() {")
            .indent();

        compiler
            .addDebugInfo(parent)
            .writeLine("parent = loadTemplate(")
            .subCompile(parent)
            .writeRaw("\"" + getFileName() + "\");\n\n");

        for (String block : blocks) {
            compiler.writeLine("blocks.put(\"" + block + "\", this::block_" + block);
        }
    }

    protected void compileClassFooter(ClassCompiler compiler) {
        compiler
                .writeLine("public String getTemplateName() {")
                .indent()
                    .write("return ")
                    .writeString(getFileName())
                    .writeRaw(";\n")
                .unIndent()
                .writeLine("}")
                .unIndent()
                .writeLine("}");
    }

    protected void compileRender(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler
                .writeLine("protected String doRender(Context context) throws TwigException {")
                    .indent()
                    .writeLine("java.util.Map<String, Object> tmpForParent;")
                    .writeLine("String output = \"\";")
                    .subCompile(this.getBodyNode())
                .writeLine("return output;")
                .unIndent()
                .writeLine("}");
    }

    public Node getBodyNode() {
        return bodyNode;
    }

    public void setBodyNode(Node bodyNode) {
        this.bodyNode = bodyNode;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
