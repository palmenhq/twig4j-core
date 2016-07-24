package org.twig.syntax;

import org.twig.exception.SyntaxErrorException;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {
    private List<Token> tokens;
    private Integer current = -1;
    private String filename;

    /**
     * Constructs an empty tokens stream
     */
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
     * @param filename The name of the template which the tokens are associated with
     */
    public TokenStream(List<Token> tokens, String filename) {
        this.tokens = tokens;
        this.filename = filename;
    }

    /**
     * Constructs a token stream with file/template name
     * @param filename The name of the template which the tokens are associated with
     */
    public TokenStream(String filename) {
        this.tokens = new ArrayList<>();
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
     * Sneak peak at the next token
     * @return The found token
     * @throws SyntaxErrorException If the next token doesn't exist
     */
    public Token look() throws SyntaxErrorException {
        try {
            return tokens.get(this.current + 1);
        } catch (IndexOutOfBoundsException e) {
            throw SyntaxErrorException.unexpectedEndOfTemplate(filename, tokens.get(current).getLine(), e);
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

    /**
     * Get all tokens (not recommended to use)
     * @return All tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Set all tokens
     * @param tokens All tokens
     */
    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Get the template name
     * @return filename The name of the template which the tokens are associated with
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the template name
     * @param filename The name of the template which the tokens are associated with
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
