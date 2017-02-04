package org.twigjava.syntax;

/**
 * Options for the Lexer (i.e. comment and block syntax)
 */
public class LexerOptions {
    private String commentOpen = "{#";
    private String commentClose = "#}";
    private String blockOpen = "{%";
    private String blockClose = "%}";
    private String variableOpen = "{{";
    private String variableClose = "}}";
    private String whitespaceTrim = "-";
    private String interpolationOpen = "#{";
    private String interpolationClose = "}";

    /**
     * The comment opening tag (default "{#")
     *
     * @return Comment opening tag
     */
    public String getCommentOpen() {
        return commentOpen;
    }

    /**
     * Set the comment opening tag to something other than default
     *
     * @param commentOpen The new comment opening tag
     */
    public void setCommentOpen(String commentOpen) {
        this.commentOpen = commentOpen;
    }

    /**
     * The comment closing tag (default "#}")
     *
     * @return Comment closing tag
     */
    public String getCommentClose() {
        return commentClose;
    }

    /**
     * Set the comment closing tag to something other than default
     *
     * @param commentClose The new comment closing tag
     */
    public void setCommentClose(String commentClose) {
        this.commentClose = commentClose;
    }

    /**
     * Get the block opening tag (default "{%}")
     *
     * @return The block opening tag
     */
    public String getBlockOpen() {
        return blockOpen;
    }

    /**
     * Set the block opening tag to something other than default
     *
     * @param blockOpen The new block opening tag
     */
    public void setBlockOpen(String blockOpen) {
        this.blockOpen = blockOpen;
    }

    /**
     * Get the block closing tag (default "%}")
     *
     * @return The block closing tag
     */
    public String getBlockClose() {
        return blockClose;
    }

    /**
     * Set the block closing tag to something other than default
     *
     * @param blockClose The new block closing tag
     */
    public void setBlockClose(String blockClose) {
        this.blockClose = blockClose;
    }

    /**
     * Get the variable opening tag (default "{{")
     *
     * @return The variable opening tag
     */
    public String getVariableOpen() {
        return variableOpen;
    }

    /**
     * Set the variable opening tag to something other than default
     *
     * @param variableOpen The new variable opening tag
     */
    public void setVariableOpen(String variableOpen) {
        this.variableOpen = variableOpen;
    }

    /**
     * Get the variable closing tag (default "}}")
     *
     * @return The variable closing tag
     */
    public String getVariableClose() {
        return variableClose;
    }

    /**
     * Set the variable closing tag to something other than default
     *
     * @param variableClose The new variable closing tag
     */
    public void setVariableClose(String variableClose) {
        this.variableClose = variableClose;
    }

    /**
     * Get the whitespace trim (default "-")
     *
     * @return The whitespace trim
     */
    public String getWhitespaceTrim() {
        return whitespaceTrim;
    }

    /**
     * Set the whitespace trim to something other than default
     *
     * @param whitespaceTrim The new whitespace trim
     */
    public void setWhitespaceTrim(String whitespaceTrim) {
        this.whitespaceTrim = whitespaceTrim;
    }

    /**
     * Get the string interpolation opening tag (default "#{")
     *
     * @return The string interpolation opening tag
     */
    public String getInterpolationOpen() {
        return interpolationOpen;
    }

    /**
     * Set the string interpolation opening tag to something other than default
     *
     * @param interpolationOpen The new string interpolation opening tag
     */
    public void setInterpolationOpen(String interpolationOpen) {
        this.interpolationOpen = interpolationOpen;
    }

    /**
     * Get the string interpolation closing tag (default "}")
     *
     * @return The interpolation closing tag
     */
    public String getInterpolationClose() {
        return interpolationClose;
    }

    /**
     * Set the string interpolation closing tag to something other than default
     *
     * @param interpolationClose The new string interpolation closing tag
     */
    public void setInterpolationClose(String interpolationClose) {
        this.interpolationClose = interpolationClose;
    }
}
