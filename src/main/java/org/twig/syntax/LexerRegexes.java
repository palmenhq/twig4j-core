package org.twig.syntax;

import java.util.List;
import java.util.regex.Pattern;

public class LexerRegexes {
    private LexerOptions options;
    private List<String> unaryOperators;
    private List<String> binaryOperators;

    public LexerRegexes(LexerOptions options, List<String> unaryOperators, List<String> binaryOperators) {
        this.options = options;
        this.unaryOperators = unaryOperators;
        this.binaryOperators = binaryOperators;
    }

    public Pattern getLexVariableEnd() {
        String pattern = "^\\s*"
                + Pattern.quote(options.getWhitespaceTrim() + options.getVariableClose())
                + "\\s*|^\\s*"
                + Pattern.quote(options.getVariableClose());

        return Pattern.compile(pattern);
    }

    public Pattern getLexBlockEnd() {
        String pattern = "^\\s*(?:"
                + Pattern.quote(options.getWhitespaceTrim() + options.getBlockClose())
                + "\\s*|^\\s*"
                + Pattern.quote(options.getBlockClose())
                + ")\\n?";

        return Pattern.compile(pattern);
    }

    public Pattern getLexRawData() {
        String pattern = "("
                + Pattern.quote(options.getBlockOpen() + options.getWhitespaceTrim())
                + "|"
                + Pattern.quote(options.getBlockOpen())
                + ")\\s*(?:end%s)\\s*(?:"
                + Pattern.quote(options.getWhitespaceTrim() + options.getBlockClose())
                + "\\s*|\\s*"
                + Pattern.quote(options.getBlockClose())
                + ")";

        return Pattern.compile(pattern, Pattern.DOTALL);
    }

    public Pattern getOperator() {
        String pattern = "";

        // TODO

        return Pattern.compile(pattern);

    }

    public Pattern getLexCommentEnd() {
        String pattern = "(?:"
                + Pattern.quote(options.getWhitespaceTrim() + options.getCommentClose())
                + "\\s*|"
                + Pattern.quote(options.getCommentClose())
                + ")\\n?";

        return Pattern.compile(pattern, Pattern.DOTALL);
    }

    public Pattern getLexBlockRaw() {
        String pattern = "^\\s*(raw|verbatim)\\s*(?:"
                + Pattern.quote(options.getWhitespaceTrim() + options.getBlockClose())
                + "\\s*|\\s*"
                + Pattern.quote(options.getBlockClose())
                + ")";

        return Pattern.compile(pattern, Pattern.DOTALL);
    }

    public Pattern getBlockLine() {
        String pattern = "^\\s*line\\s+(\\d+)\\s*"
                + Pattern.quote(options.getBlockClose());

        return Pattern.compile(pattern, Pattern.DOTALL);
    }

    public Pattern getLexTokensStart() {
        String pattern = "("
                + Pattern.quote(options.getVariableOpen())
                + "|" + Pattern.quote(options.getBlockOpen())
                + "|" + Pattern.quote(options.getCommentOpen())
                + ")("
                + Pattern.quote(options.getWhitespaceTrim())
                + ")?";

        return Pattern.compile(pattern, Pattern.DOTALL);
    }

    public Pattern getInterpolationStart() {
        String pattern = "^"
                + Pattern.quote(options.getInterpolationOpen())
                + "\\s*";

        return Pattern.compile(pattern);
    }

    public Pattern getInterpolationEnd() {
        String pattern = "^\\s*"
                + Pattern.quote(options.getInterpolationClose());

        return Pattern.compile(pattern);
    }

    public Pattern getExpressionName() {
        return Pattern.compile("^[a-zA-Z_\\x7f-\\xff][a-zA-Z0-9_\\x7f-\\xff]*");
    }

    public Pattern getExpressionNumber() {
        return Pattern.compile("^[0-9]+(?:\\.[0-9]+)?");
    }

    public Pattern getExpressionString() {
        return Pattern.compile("^\"([^#\"\\\\]*(?:\\\\.[^#\"\\\\]*)*)\"|'([^'\\\\]*(?:\\\\.[^'\\\\]*)*)'", Pattern.DOTALL);
    }

    public void setOptions(LexerOptions options) {
        this.options = options;
    }

    public void setUnaryOperators(List<String> unaryOperators) {
        this.unaryOperators = unaryOperators;
    }

    public void setBinaryOperators(List<String> binaryOperators) {
        this.binaryOperators = binaryOperators;
    }
}
