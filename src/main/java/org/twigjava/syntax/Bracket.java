package org.twigjava.syntax;

/**
 * Represents a level of nesting, i.e. a string or a bracket.
 */
public class Bracket {
    String type;
    Integer line;

    /**
     * Constructor
     * @param type The nesting character(s)
     * @param line The line number
     */
    public Bracket(String type, Integer line) {
        this.type = type;
        this.line = line;
    }

    /**
     * Gets the nesting character(s)
     * @return The nesting character(s)
     */
    public String getType() {
        return type;
    }

    /**
     * Sets the nesting character(s)
     * @param type The nesting character(s)
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * Gets the line number the nesting begins on
     * @return The line number
     */
    public Integer getLine() {
        return line;
    }

    /**
     * Sets the line number the nesting begins on
     * @param line The line number
     */
    public void setLine(Integer line) {
        this.line = line;
    }
}
