package org.twig.syntax;

public class Token {
    public static enum Type {
        EOF, TEXT,
        BLOCK_START, BLOCK_END,
        VAR_START, VAR_END,
        NAME, NUMBER, STRING,
        OPERATOR, PUNCTUATION,
        INTERPLATION_START, INTERPOLATION_END
    }

    Type type;
    String value;
    Integer line;

    /**
     * Token constructor
     *
     * @param type  The token type
     * @param value The token value
     * @param line  The source line
     */
    public Token(Type type, String value, Integer line) {
        this.type = type;
        this.value = value;
        this.line = line;
    }

    /**
     * Tests the token for a type
     * @param type The token type to test
     * @return Whether the provided type equals the current token type
     */
    public Boolean test(Type type) {
        return type == this.type;
    }

    /**
     * Tests the token for a type and value
     * @param type The token type to test
     * @param value The token value to test
     * @return Whether the provided type and value equals the current token
     */
    public Boolean test(Type type, String value) {
        return type == this.type && value.equals(this.value);
    }

    /**
     * Returns the english representation of a given type
     *
     * @param type The token type
     * @return Human readable token type
     * @throws Exception
     */
    public static String typeToEnglish(Type type) {
        switch (type) {
            case EOF:
                return "end of template";
            case TEXT:
                return "text";
            case BLOCK_START:
                return "begin of block statement";
            case BLOCK_END:
                return "end of block statement";
            case VAR_START:
                return "begin of print statement";
            case VAR_END:
                return "end of print statement";
            case NAME:
                return "name";
            case NUMBER:
                return "number";
            case STRING:
                return "string";
            case OPERATOR:
                return "operator";
            case PUNCTUATION:
                return "punctuation";
            case INTERPLATION_START:
                return "begin of string interpolation";
            case INTERPOLATION_END:
                return "end of string interpolation";
            default:
                throw new RuntimeException(String.format("Token of type \"%s\" does not exist.", type.toString()));

        }
    }

    /**
     * Check whether the token is the same type as provided type
     *
     * @param type The type to test
     * @return
     */
    public boolean is(Type type) {
        return this.type == type;
    }

    /**
     * Check whether the token is the same type as provided type and has the same value as the provided value
     *
     * @param type The type to test
     * @param value The value to test
     * @return
     */
    public boolean is(Type type, String value) {
        return this.type == type && this.value.equals(value);
    }

    @Override
    public String toString() {
        return this.type.toString();
    }

    /**
     * Gets token type
     *
     * @return The token type
     */
    public Type getType() {
        return type;
    }

    /**
     * Sets the token type
     *
     * @param type The token type
     */
    public void setType(Type type) {
        this.type = type;
    }

    /**
     * Gets the token value
     *
     * @return The token value
     */
    public String getValue() {
        return value;
    }

    /**
     * Sets the token value
     *
     * @param value The token value
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Gets the source line
     *
     * @return The source line
     */
    public Integer getLine() {
        return line;
    }

    /**
     * Sets the source line
     *
     * @param line The source line
     */
    public void setLine(Integer line) {
        this.line = line;
    }
}
