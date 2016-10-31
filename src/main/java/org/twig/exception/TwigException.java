package org.twig.exception;

public class TwigException extends Exception {
    private String rawMessage;
    private Integer lineNumber;
    private String templateName;

    /**
     * Create a twig exception without any template info
     * @param rawMessage The error message
     */
    public TwigException(String rawMessage) {
        this.rawMessage = rawMessage;
    }

    /**
     * Create a twig exception without any template info
     * @param rawMessage The error message
     * @param cause The exception that previously followed this one
     */
    public TwigException(String rawMessage, Throwable cause) {
        this.rawMessage = rawMessage;
        initCause(cause);
    }

    /**
     * Create a twig exception with file name included
     * @param rawMessage The error message
     * @param templateName The filename the error occurred in
     */
    public TwigException(String rawMessage, String templateName) {
        this.rawMessage = rawMessage;
        this.templateName = templateName;
    }

    /**
     * Create a twig exception with file name and line number included
     * @param rawMessage The error message
     * @param templateName The filename the error occurred in
     * @param lineNumber The line number the error occurred at
     */
    public TwigException(String rawMessage, String templateName, Integer lineNumber) {
        this.rawMessage = rawMessage;
        this.templateName = templateName;
        this.lineNumber = lineNumber;
    }

    /**
     * Create a twig exception with file name and line number included
     * @param rawMessage The error message
     * @param templateName The filename the error occurred in
     * @param lineNumber The line number the error occurred at
     * @param cause The exception that previously followed this one
     */
    public TwigException(String rawMessage, String templateName, Integer lineNumber, Throwable cause) {
        this.rawMessage = rawMessage;
        this.lineNumber = lineNumber;
        this.templateName = templateName;
        initCause(cause);
    }

    /**
     * Gets the formatted message
     * @return The message
     */
    public String getMessage() {
        String message = this.rawMessage;
        Boolean endsWithDot = false;
        Boolean endsWithQuestionMark = false;

        // Check if the last character is a dot
        if (message.charAt(message.length()-1) == '.') {
            message = message.substring(0, message.length()-1);
            endsWithDot = true;
        }

        // Check if the last character is a question mark
        if (message.charAt(message.length()-1) == '?') {
            message = message.substring(0, message.length()-1);
            endsWithQuestionMark = true;
        }

        if (templateName != null) {
            message += " in \"" + templateName + "\"";
        }

        if (lineNumber != null && lineNumber > -1) {
            message += " at line " + lineNumber;
        }

        if (endsWithDot) {
            message += ".";
        }
        if (endsWithQuestionMark) {
            message += "?";
        }

        return message;
    }

    /**
     * Get the line number where the error occurred
     * @return The line number
     */
    public Integer getLineNumber() {
        return lineNumber;
    }

    /**
     * Set the line number wher the error occurred
     * @param lineNumber The line number
     */
    public void setLineNumber(Integer lineNumber) {
        this.lineNumber = lineNumber;
    }

    /**
     * Get the filename where the error occurred
     * @return The filename
     */
    public String getTemplateName() {
        return templateName;
    }

    /**
     * Set the filename where the error occurred
     * @param templateName The filename
     */
    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }
}
