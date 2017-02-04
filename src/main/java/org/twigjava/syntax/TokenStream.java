package org.twigjava.syntax;

import org.twigjava.exception.SyntaxErrorException;

import java.util.ArrayList;
import java.util.List;

public class TokenStream {
    private List<Token> tokens;
    private Integer current = 0;
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

    /**
     * @see #expect(Token.Type, String, String)
     *
     * @param type The token type to expect
     *
     * @return The current token
     *
     * @throws SyntaxErrorException If the expectation is incorrect
     */
    public Token expect(Token.Type type) throws SyntaxErrorException {
        return expect(type, null, null);
    }

    /**
     * @see #expect(Token.Type, String, String)
     *
     * @param type The token type to expect
     * @param value The token value to expect
     *
     * @return The current token
     *
     * @throws SyntaxErrorException If the expectation is incorrect
     */
    public Token expect(Token.Type type, String value) throws SyntaxErrorException {
        return expect(type, value, null);
    }

    /**
     * Tests a token, moves on and returns the first one, or throws a syntax error if expectation fails.
     *
     * @param type The token type to expect
     * @param value The token value to expect
     * @param message What to say if the expectation fails
     *
     * @return The current token
     *
     * @throws SyntaxErrorException If the expectation is incorrect
     */
    public Token expect(Token.Type type, String value, String message) throws SyntaxErrorException {
        Token token = tokens.get(current);
        boolean tokenIsTypeAndMaybeValue;

        if (value == null) {
            tokenIsTypeAndMaybeValue = token.is(type);
        } else {
            tokenIsTypeAndMaybeValue = token.is(type, value);
        }

        if (!tokenIsTypeAndMaybeValue) {
            StringBuilder exceptionMessage = new StringBuilder();
            if (message != null) {
                exceptionMessage.append(message).append(". ");
            }
            exceptionMessage.append(String.format("Unexpected token \"%s\" of value \"%s\"", Token.typeToEnglish(token.type), token.getValue()));
            exceptionMessage.append(String.format(" (\"%s\" expected", Token.typeToEnglish(type)));
            if (value != null) {
                exceptionMessage.append(String.format(" with value \"%s\"", value));
            }
            exceptionMessage.append(")");

            throw new SyntaxErrorException(exceptionMessage.toString(), this.filename, token.getLine());
        }

        next();

        return token;
    }

    /**
     * Sets the pointer to the next token and returns the old one.
     *
     * @return The token before nexting
     *
     * @throws SyntaxErrorException If there is no next token (= eof is here)
     */
    public Token next() throws SyntaxErrorException {
        try {
            current ++;
            return tokens.get(current - 1);
        } catch (IndexOutOfBoundsException e) {
            throw SyntaxErrorException.unexpectedEndOfTemplate(filename, tokens.get(current-2).getLine(), e);
        }
    }

    /**
     * Check whether the next token type is of the provided type
     *
     * @param type The type to check
     *
     * @return Whether the next token type is the same as the provided one
     */
    public boolean nextIs(Token.Type type) {
        try {
            return tokens.get(current + 1).is(type);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Check whether the next token type is of the provided type and value
     *
     * @param type The type to check
     * @param value The contents to check
     *
     * @return Whether the next token type is the same as the provided one
     */
    public boolean nextIs(Token.Type type, String value) {
        try {
            return tokens.get(current + 1).is(type, value);
        } catch (IndexOutOfBoundsException e) {
            return false;
        }
    }

    /**
     * Sneak peak at the next token
     *
     * @return The found token
     *
     * @throws SyntaxErrorException If the next token doesn't exist
     */
    public Token look() throws SyntaxErrorException {
        try {
            return tokens.get(this.current);
        } catch (IndexOutOfBoundsException e) {
            throw SyntaxErrorException.unexpectedEndOfTemplate(filename, tokens.get(current).getLine(), e);
        }
    }

    /**
     * Sneak peak at a token at some specific index
     *
     * @param index The index to look at
     *
     * @return The found token
     *
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
     * Returns the current token
     *
     * @return The token
     */
    public Token getCurrent() {
        return tokens.get(current);
    }

    /**
     * Returns whether is at the end of the file
     *
     * @return Whether current token is EOF
     */
    public Boolean isEOF() {
        return getCurrent().getType() == Token.Type.EOF;
    }

    /**
     * Adds a new token
     *
     * @param token The token to add
     */
    public void add(Token token) {
        tokens.add(token);
    }

    /**
     * Get all tokens (not recommended to use)
     *
     * @return All tokens
     */
    public List<Token> getTokens() {
        return tokens;
    }

    /**
     * Set all tokens
     *
     * @param tokens All tokens
     */
    public void setTokens(List<Token> tokens) {
        this.tokens = tokens;
    }

    /**
     * Get the template name
     *
     * @return filename The name of the template which the tokens are associated with
     */
    public String getFilename() {
        return filename;
    }

    /**
     * Set the template name
     *
     * @param filename The name of the template which the tokens are associated with
     */
    public void setFilename(String filename) {
        this.filename = filename;
    }
}
