package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.control.IfStatement;
import org.twig.syntax.parser.node.type.expression.Expression;

import java.util.*;

public class For extends Node {
    protected ForLoop loop;

    public For(Settings settings, Integer line, String tag) {
        super(line);

        this.loop = new ForLoop(line, tag);
        Node body = new Node(Arrays.asList(settings.getBody(), this.loop), new HashMap<>(), line, tag);

        if (settings.getIfExpr() != null) {
            List<Node> ifStatementParams = new ArrayList<>();
            ifStatementParams.add(settings.getIfExpr());
            ifStatementParams.add(body);
            body = new IfStatement(ifStatementParams, line, tag);
        }

        List<Node> nodes = Arrays.asList(
                settings.getSeq(), // Sequence = 0
                body, // Body = 1
                settings.getElseBody() // Elsebody = 2
        );
        attributes.put("key_target", settings.getKeyTarget());
        attributes.put("value_target", settings.getValueTarget());
        // TODO add the Optimizer node visitor that removes with_loop if it's not used
        attributes.put("with_loop", true);
        attributes.put("ifExpr", null != settings.getIfExpr());

        this.nodes = nodes;
        setTag(tag);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.addDebugInfo(this)
                .writeLine(putInContext("_parent", "context.clone()"))
                .write("((java.util.Map<String, Object>)context).put(\"_seq\", org.twig.extension.Core.ensureIterable(");
            this.getNode(0).compile(compiler); // Node 0 = _seq
        compiler
                .writeRaw("));\n")
                .writeLine(putInContext("_iterated", "false"))
                .writeLine(putInContext("_loop_internal", "(new org.twig.util.HashMap())"))
                .writeLine("((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"_loop_internal\")).put(\"iterator_index\", 0);");

        ;

        if (((Boolean)this.getAttribute("with_loop"))) {
            compiler
                    .writeLine(putInContext("loop", "(new org.twig.util.HashMap())"))
                    .writeLine("((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"loop\"))")
                    .indent()
                        .writeLine(".put(\"parent\", ((Object)context.get(\"_parent\")))")
                        .writeLine(".put(\"index0\", 0)")
                        .writeLine(".put(\"index\", 1)")
                        .writeLine(".put(\"_iterator_index\", 0)")
                        .writeLine(".put(\"first\", true);")
                    .unIndent()
                    // Make an extra line break before the for loop
                    .writeRaw("\n");
        }

        // TODO revindex

        String randomVariableName = getRandomVariableName();
        // Do the loop
        compiler
                .writeLine("// " + randomVariableName + " = " + ((String)getAttribute("value_target")))
                .writeLine("for (Object " + randomVariableName + " : ((Iterable)(context.get(\"_seq\")))) {")
                .indent();

        compileKeyValueTarget(compiler, randomVariableName);

        this.getNode(1).compile(compiler); // Node 1 = for body

        // Increase the internal iterator index by 1
        compiler
                .writeLine("((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"_loop_internal\"))")
                .indent()
                    .writeLine(".put(\"iterator_index\",")
                    .indent()
                        .writeLine("((Integer)((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"_loop_internal\")).get(\"iterator_index\")) + 1")
                    .unIndent()
                    .writeLine(");")
                .unIndent();

        compiler
                .unIndent()
                .writeLine("}")
        ;

        // Reset loop
        compiler
                .writeLine("tmpForParent = new java.util.HashMap<String, Object>();")
                .writeLine("tmpForParent.putAll(((java.util.Map<String, Object>)((java.util.Map<String, Object>)context).get(\"_parent\")));")
                .writeLine("context.remove(\"_seq\");")
                .writeLine("context.remove(\"_iterated\");")
                .writeLine("context.remove(\"" + getAttribute("key_target") + "\");")
                .writeLine("context.remove(\"" + getAttribute("value_target") + "\");")
                .writeLine("context.remove(\"_parent\");")
                .writeLine("context.remove(\"loop\");")
                .writeLine("((java.util.Map<String, Object>)context).putAll(tmpForParent);")
        ;
    }

    /**
     * Set the key and value variables
     * @param compiler
     */
    protected void compileKeyValueTarget(ClassCompiler compiler, String valueVariableName) throws TwigRuntimeException {
        compiler.writeLine(putInContext((String)getAttribute("value_target"), valueVariableName));

        compiler
                .writeLine("if (context.get(\"_seq\") instanceof java.util.List) {")
                .indent()
                    .writeLine(putInContext(((String)getAttribute("key_target")), "((org.twig.util.HashMap)((java.util.Map<String, Object>)context).get(\"_loop_internal\")).get(\"iterator_index\")"))
                .unIndent()
                .writeLine("}");
    }

    /**
     * Make an assignment to the context
     * @param key The key to put in, always a string
     * @param value The value variable name to put in
     * @return The put statement
     */
    protected String putInContext(String key, String value) {
        return "((java.util.Map<String, Object>)context).put(\"" + key + "\", " + value + ");";
    }

    /**
     * Creates a 15 character long variable name
     * @return
     */
    protected String getRandomVariableName() {
        String characters = "abcdefghijklmnopqrstuvwxyz";
        StringBuilder randomString = new StringBuilder();
        Random random = new Random();

        for (Integer i = 0; i < 15; i++) {
            randomString.append(characters.charAt(random.nextInt(26)));
        }

        return randomString.toString();
    }

    /**
     * Settings for the for node - used instead of a million constructor arguments
     */
    public static class Settings {
        private String keyTarget = "_key";
        private String valueTarget;
        private Expression seq;
        private Expression ifExpr;
        private Node body;
        private Node elseBody;

        /**
         * Get the target variable for the current key in the loop
         *
         * @return The target key variable
         */
        public String getKeyTarget() {
            return keyTarget;
        }

        /**
         * Set the target variable for the current key in the loop
         *
         * @param keyTarget The target variable
         * @return this
         */
        public Settings setKeyTarget(String keyTarget) {
            this.keyTarget = keyTarget;
            return this;
        }

        /**
         * Get the target variable for the current value in the loop
         *
         * @return The variable
         */
        public String getValueTarget() {
            return valueTarget;
        }

        /**
         * Set the target variable for the current value in the loop
         *
         * @param valueTarget The target variable
         * @return this
         */
        public Settings setValueTarget(String valueTarget) {
            this.valueTarget = valueTarget;
            return this;
        }

        /**
         * Get thing to iterate over
         *
         * @return
         */
        public Expression getSeq() {
            return seq;
        }

        /**
         * Set thing to iterate over
         *
         * @param seq The thing
         * @return this
         */
        public Settings setSeq(Expression seq) {
            this.seq = seq;
            return this;
        }

        /**
         * Get the if expression (if any) that helps filtering the loop
         *
         * @return The if expression
         */
        public Expression getIfExpr() {
            return ifExpr;
        }

        /**
         * Set the if expression that helps filtering the loop
         *
         * @param ifExpr The expression
         * @return this
         */
        public Settings setIfExpr(Expression ifExpr) {
            this.ifExpr = ifExpr;
            return this;
        }

        /**
         * Get the loop body
         *
         * @return The body
         */
        public Node getBody() {
            return body;
        }

        /**
         * Set the loop body
         *
         * @param body The body
         * @return this
         */
        public Settings setBody(Node body) {
            this.body = body;
            return this;
        }

        /**
         * Get the else body (used if the loop is empty)
         *
         * @return
         */
        public Node getElseBody() {
            return elseBody;
        }

        /**
         * Set the else body (usid if th eloop is empty)
         *
         * @param elseBody The else body
         * @return this
         */
        public Settings setElseBody(Node elseBody) {
            this.elseBody = elseBody;
            return this;
        }
    }
}
