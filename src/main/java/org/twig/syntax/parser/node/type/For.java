package org.twig.syntax.parser.node.type;

import org.twig.compiler.ClassCompiler;
import org.twig.exception.LoaderException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.parser.node.Node;
import org.twig.syntax.parser.node.type.expression.Expression;

import java.util.Arrays;
import java.util.List;

public class For extends Node {
    public For(Settings settings, Integer line, String tag) {
        super(line);

        List<Node> nodes = Arrays.asList(
                settings.getSeq(), // Sequence = 0
                settings.getBody(), // Body = 1
                settings.getElseBody() // Elsebody = 2
        );
        attributes.put("key_target", settings.getKeyTarget());
        attributes.put("value_target", settings.getValueTarget());
        attributes.put("with_loop", true);
        attributes.put("ifExpr", null != settings.getIfExpr());

        this.nodes = nodes;
        setTag(tag);
    }

    @Override
    public void compile(ClassCompiler compiler) throws LoaderException, TwigRuntimeException {
        compiler.addDebugInfo(this)
                .writeLine(putInContext("_parent", "context"))
                .write("((java.util.Map<String, Object>)context).put(\"_seq\", org.twig.extension.Core.ensureIterable(");
            this.getNode(0).compile(compiler); // Node 0 = _seq
        compiler
                .writeLine("));");
//                .writeLine("context.put(\"_iterated\", false);");
//                .writeLine("context.put(\"_loop\", (new org.twig.util.HashMap())")
//                .indent()
//                    .writeLine(".put(\"parent\", context.get(\"_parent\"))")
//                    .writeLine(".put(\"index0\", 0)")
//                    .writeLine(".put(\"index\", 1)")
//                    .writeLine(".put(\"first\", true)")

        // Do the loop
        compiler
                .writeLine("for (Object value : ((Iterable)(context.get(\"_seq\")))) {")
                .indent()
                // TODO set key
                .writeLine(putInContext((String)getAttribute("value_target"), "value"));
        this.getNode(1).compile(compiler); // Node 1 = for body
        compiler
                .unIndent()
                .writeLine("}");

        // Reset loop
        compiler
                .writeLine(putInContext("_old_parent", "((Object)context.get(\"_parent\"))"))
                .writeLine("context.remove(\"_seq\");")
                .writeLine("context.remove(\"_iterated\");")
                .writeLine("context.remove(\"" + getAttribute("key_target") + "\");")
                .writeLine("context.remove(\"" + getAttribute("value_target") + "\");")
                .writeLine("context.remove(\"_parent\");")
                .writeLine("((java.util.Map<String, Object>)context).putAll(((java.util.HashMap)context.get(\"_old_parent\")));")
                .writeLine("context.remove(\"_old_parent\");")
        ;
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
