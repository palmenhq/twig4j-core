package org.twig4j.core.exception;

import org.twig4j.core.syntax.Lexer;
import org.twig4j.core.syntax.Token;

public class SyntaxErrorException extends TwigException {
    /**
     * {@inheritDoc}
     */
    public SyntaxErrorException(String rawMessage) {
        super(rawMessage);
    }

    /**
     * {@inheritDoc}
     */
    public SyntaxErrorException(String rawMessage, Throwable cause) {
        super(rawMessage, cause);
    }

    /**
     * {@inheritDoc}
     */
    public SyntaxErrorException(String rawMessage, String templateName) {
        super(rawMessage, templateName);
    }

    /**
     * {@inheritDoc}
     */
    public SyntaxErrorException(String rawMessage, String templateName, Integer lineNumber) {
        super(rawMessage, templateName, lineNumber);
    }

    /**
     * {@inheritDoc}
     */
    public SyntaxErrorException(String rawMessage, String templateName, Integer lineNumber, Throwable cause) {
        super(rawMessage, templateName, lineNumber, cause);
    }

    /**
     * Create an unexpected end of template exceptino
     * @param templateName The name of the template/file the error occurred in
     * @param lineNumber The line the error occurred on
     * @param cause For the stack trace
     * @return The exception
     */
    public static SyntaxErrorException unexpectedEndOfTemplate(String templateName, Integer lineNumber, Throwable cause) {
        return new SyntaxErrorException("Unexpected end of template.", templateName, lineNumber, cause);
    }

    /**
     * Create an unexpected end of template exception
     * @param templateName The name of the template/file the error occurred in
     * @param lineNumber The line the error occurred on
     * @return The exception
     */
    public static SyntaxErrorException unexpectedEndOfTemplate(String templateName, Integer lineNumber) {
        return new SyntaxErrorException("Unexpected end of template.", templateName, lineNumber);
    }

    /**
     * Create an unclosed comment error
     * @param templateName The name of the template/file the error occurred in
     * @param lineNumber The line the error occurred on
     * @return The exception
     */
    public static SyntaxErrorException unclosedComment(String templateName, Integer lineNumber) {
        return new SyntaxErrorException("Unclosed comment.", templateName, lineNumber);
    }

    /**
     * Create an unclosed tag exception
     * @param tagType The type of tag
     * @param templateName The name of the template/file the error occurred in
     * @param lineNumber The line the error occurred on
     * @return The exception
     */
    public static SyntaxErrorException unclosedTag(Lexer.State tagType, String templateName, Integer lineNumber) {
        String tagTypeInEnglish = "undefined tag";

        switch(tagType) {
            case BLOCK:
                tagTypeInEnglish = "block";
                break;
            case VARIABLE:
                tagTypeInEnglish = "variable";
                break;
        }

        return new SyntaxErrorException("Unclosed " + tagTypeInEnglish + ".", templateName, lineNumber);
    }

    /**
     * Create an something exception
     * @param something Whatever is unclosed
     * @param templateName The name of the template/file the error occurred in
     * @param lineNumber The line the error occurred on
     * @return The exception
     */
    public static SyntaxErrorException unclosedSomething(String something, String templateName, Integer lineNumber) {
        return new SyntaxErrorException("Unclosed " + something + ".", templateName, lineNumber);
    }

    /**
     * Create an unexpected character error
     * @param character The unexpected character
     * @param templateName The name of the template/file the error occurred in
     * @param lineNumber The line the error occurred on
     * @return The exception
     */
    public static SyntaxErrorException unexpectedCharacter(String character, String templateName, Integer lineNumber) {
        return new SyntaxErrorException("Unexpected character \"" + character + "\".", templateName, lineNumber);
    }

    /**
     * Create an unexpected token error
     * @param token The token that was unexpected
     * @param templateName The name of teh template/file the error occured in
     * @param lineNumber The line the error occured on
     * @return The exception
     */
    public static SyntaxErrorException unexpectedToken(Token token, String templateName, Integer lineNumber) {
        return new SyntaxErrorException(
                String.format("Unexpected token \"%s\" of value \"%s\".", Token.typeToEnglish(token.getType()), token.getValue()),
                templateName,
                lineNumber
            );
    }
}
