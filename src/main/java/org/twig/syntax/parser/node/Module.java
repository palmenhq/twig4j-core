package org.twig.syntax.parser.node;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.Compilable;
import org.twig.exception.LoaderException;

import java.util.ArrayList;

public class Module implements Compilable {
    ArrayList<Node> bodyNodes = new ArrayList<>();

    protected String fileName = "";

    public Module(ArrayList<Node> bodyNodes) {
        this.bodyNodes = bodyNodes;
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException {
        compileClassHeader(compiler);

        compileClassFooter(compiler);
    }

    public void compileClassHeader(ClassCompiler compiler) throws LoaderException {
        String className = compiler.getEnvironment().getTemplateClass(this.fileName);
        String baseClass = compiler.getEnvironment().getTemplateBaseClass();

        compiler
                .writeLine("package org.twig.template;\n")
                .writeLine("/**")
                .writeLine(" * ".concat(this.fileName))
                .writeLine(" */")
                .write("public class ".concat(className))
                .writeLine(" extends ".concat(baseClass).concat(" {"))
                .indent();
    }

    public void compileClassFooter(ClassCompiler compiler) {
        compiler
                .unIndent()
                .writeLine("}");
    }

    public ArrayList<Node> getBodyNodes() {
        return bodyNodes;
    }

    public void setBodyNodes(ArrayList<Node> bodyNodes) {
        this.bodyNodes = bodyNodes;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
