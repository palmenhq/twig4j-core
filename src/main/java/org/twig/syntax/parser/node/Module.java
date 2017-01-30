package org.twig.syntax.parser.node;

import org.twig.compiler.ClassCompiler;
import org.twig.compiler.Compilable;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.type.Block;

import java.util.Map;

public class Module implements Compilable {
    protected Node bodyNode;
    protected Node parent;
    protected Map<String, Block> blocks;

    protected String fileName = "";

    public Module(Node bodyNode) {
        this.bodyNode = bodyNode;
    }

    public Module(Node bodyNode, Node parent, Map<String, Block> blocks, String fileName) {
        this.bodyNode = bodyNode;
        this.parent = parent;
        this.blocks = blocks;
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

        compileBlocks(compiler);

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
            .writeLine("public " + className + "(org.twig.Environment environment) throws TwigException {")
            .indent()
                .writeLine("this.environment = environment;\n")
                .addDebugInfo(parent)
                .write("parent = loadTemplate(")
                .subCompile(parent)
                .writeRaw(", \"" + getFileName() + "\", 1, null);\n\n");

        for (Map.Entry block : blocks.entrySet()) {
            compiler.writeLine("blocks.put(\"" + block.getKey() + "\", this::block_" + block.getKey() + ");");
        }

        compiler
            .unIndent()
            .writeLine("}\n");
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

    protected void compileBlocks(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        if (blocks == null) {
            return;
        }

        for (Map.Entry<String, Block> block : blocks.entrySet()) {
            compiler.subCompile(block.getValue());
        }
    }

    public Node getBodyNode() {
        return bodyNode;
    }

    public void setBodyNode(Node bodyNode) {
        this.bodyNode = bodyNode;
    }

    public Node getParent() {
        return parent;
    }

    public Module setParent(Node parent) {
        this.parent = parent;
        return this;
    }

    public Map<String, Block> getBlocks() {
        return blocks;
    }

    public Module setBlocks(Map<String, Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}
