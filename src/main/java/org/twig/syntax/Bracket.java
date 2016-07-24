package org.twig.syntax;

public class Bracket {
    String type;
    Integer line;

    public Bracket(String type, Integer line) {
        this.type = type;
        this.line = line;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getLine() {
        return line;
    }

    public void setLine(Integer line) {
        this.line = line;
    }
}
