package org.twig.syntax;

import org.twig.exception.SyntaxErrorException;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {
    private List<Token> tokens;
    private Integer current = -1;
    private String filename;

    public TokenStream() {
        tokens = new ArrayList<>();
    }

    /**
     * Constructs a token stream without file/template name
     * @param tokens The tokens
     */
    public TokenStream(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Constructs a token stream with file/template name
     * @param tokens The tokens
     * @param filename The name of the template wich the tokens are associated with
     */
    public TokenStream(List<Token> tokens, String filename) {
        this.tokens = tokens;
        this.filename = filename;
    }

    public Token next() throws SyntaxErrorException {
        try {
            current ++;
            return tokens.get(current);
        } catch (IndexOutOfBoundsException e) {
            throw SyntaxErrorException.unexpectedEndOfTemplate(filename, tokens.get(current-1).getLine(), e);
        }
    }

    /**
     * Sneak peak at a token at some specific index
     * @param index The index to look at
     * @return The found token
     * @throws SyntaxErrorException If the requested token doesn't exist
     */
    public Token look(Integer index) throws SyntaxErrorException {
        try {
            return tokens.get(index);
        } catch (IndexOutOfBoundsException e) {
            throw SyntaxErrorException.unexpectedEndOfTemplate(filename, tokens.get(current).getLine(), e);
        }
    }

    /**
     * Adds a new token
     * @param token The token to add
     */
    public void add(Token token) {
        tokens.add(token);
    }
}
