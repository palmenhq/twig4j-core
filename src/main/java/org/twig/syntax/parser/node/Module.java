package org.twig.syntax.parser.node;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.Compilable;
import org.twig.exception.LoaderException;

import java.util.ArrayList;

public class Module implements Compilable {
    Node bodyNode;

    protected String fileName = "";

    public Module(Node bodyNode) {
        this.bodyNode = bodyNode;
    }

    public Module(Node bodyNode, String fileName) {
        this.bodyNode = bodyNode;
        this.fileName = fileName;
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
        compileClassHeader(compiler);

        compileRender(compiler);

        compileClassFooter(compiler);
    }

    protected void compileClassHeader(ClassCompiler compiler) throws LoaderException {
        String className = compiler.getEnvironment().getTemplateClass(this.fileName);
        String baseClass = compiler.getEnvironment().getTemplateBaseClass();

        compiler
                .writeLine("package " + compiler.getEnvironment().getTemplatePackage() + ";\n")
                .writeLine("/**")
                .writeLine(" * ".concat(this.fileName))
                .writeLine(" */")
                .write("public class ".concat(className))
                .writeLine(" extends ".concat(baseClass).concat(" {"))
                .indent();
    }

    protected void compileClassFooter(ClassCompiler compiler) {
        compiler
                .unIndent()
                .writeLine("}");
    }

    protected void compileRender(ClassCompiler compiler) throws LoaderException {
        compiler
                .writeLine("protected String doRender(java.util.HashMap<String, String> context) {")
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
