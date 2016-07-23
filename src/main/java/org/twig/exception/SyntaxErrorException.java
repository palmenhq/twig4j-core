package org.twig.exception;

import org.twig.syntax.Lexer;

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
}
