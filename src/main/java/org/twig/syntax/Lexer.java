package org.twig.syntax;

import org.twig.exception.SyntaxErrorException;
import org.twig.utils.StringModule;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Lexes code and gives us a TokenStream
 */
public class Lexer {
    public static enum State {
        TEXT, BLOCK, VARIABLE, STRING, INTERPOLATION, COMMENT
    }

    /**
     * Represents a token position in the code
     */
    protected class TokenPosition {
        private Integer offset;
        private String group;

        public TokenPosition(Integer offset, String group) {
            this.offset = offset;
            this.group = group;
        }

        public Integer getOffset() {
            return offset;
        }

        public void setOffset(Integer offset) {
            this.offset = offset;
        }

        public String getGroup() {
            return group;
        }

        public void setGroup(String group) {
            this.group = group;
        }
    }

    // The code to tokenize
    private String code;
    // The file name of the code (used in error messages)
    private String filename;
    // From where in the code we're currently tokenizing
    private Integer cursor;
    // The current token tag position @see tagPositions
    private Integer position;
    // Positions of all found tag tokens (ie block, var etc.). Just so we don't have to step through each character of the code
    private List<TokenPosition> tagPositions;
    // On which character count the code ends
    private Integer end;
    // On which line we're currently on (used in error messages)
    private Integer line;
    // On which line the tag being tokenized is on (used in error messages)
    private Integer currentTagLine;
    // The current type of token we're analyzing
    private State state;
    // A list of the states, as states can be nestled
    private List<State> states;
    // TODO: Not sure what this thing does yet, find out
    private List<Bracket> brackets;
    // The stream of tokens to return
    private TokenStream tokenStream;
    // Options for the lexer (ie the variable and block open/close tags should look like)
    private LexerOptions options;
    // All required regexes
    private LexerRegexes regexes;

    /**
     * Constructor with default configuration
     */
    public Lexer() {
        options = new LexerOptions();
        // TODO set the operator lists
        regexes = new LexerRegexes(options, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Constructor with custom configuration
     *
     * @param options The options to use
     */
    public Lexer(LexerOptions options) {
        this.options = options;
        // TODO set the operator lists
        regexes = new LexerRegexes(this.options, new ArrayList<>(), new ArrayList<>());
    }

    /**
     * Creates tokens from the provided code
     *
     * @param code     The code to tokenize
     * @param filename The source file (used for error messages)
     * @return The stream of tokens from the source
     * @throws SyntaxErrorException If we can't keep on tokenizing for some reason (i.e. missing closing tag)
     */
    public TokenStream tokenize(String code, String filename) throws SyntaxErrorException {
        this.code = code;
        this.filename = filename;
        this.cursor = 0;
        this.end = code.length();
        this.line = 1;
        this.currentTagLine = 1;
        this.tokenStream = new TokenStream(this.filename);
        this.position = -1;
        this.tagPositions = new ArrayList<>();
        this.brackets = new ArrayList<>();
        this.states = new ArrayList<>();
        // The initial state should always be TEXT
        pushState(State.TEXT);

        // Find all tags except TEXT so we don't have to step through each character
        findTokenPositions();

        // Keep on tokenizing as long as there are still characters to tokenize
        while (this.cursor < this.end) {
            switch (this.state) {
                case TEXT:
                    lexData();
                    break;
                case VARIABLE:
                    lexVariable();
                    break;
                case BLOCK:
                    lexBlock();
                    break;
                case COMMENT:
                    lexComment();
                    break;
                case STRING:
                    lexString();
                    break;
            }
        }

        // This is the end of the file
        pushToken(Token.Type.EOF);

        // If there are unclosed elements left
        if (this.brackets.size() > 0) {
            Bracket bracket = brackets.remove(brackets.size() - 1);
            throw SyntaxErrorException.unclosedSomething(bracket.getType(), this.filename, bracket.getLine());
        }

        return this.tokenStream;
    }

    /**
     * Entry point for tokenizing - tokenizes text and changes the state to whatever the next token requires
     */
    protected void lexData() {
        // If no matching tags are left we return the rest of the template as simple text token
        if (this.position == (this.tagPositions.size() - 1)) {
            pushToken(Token.Type.TEXT, code.substring(cursor));
            this.cursor = this.end;

            return;
        }

        // Move to the next tag position
        this.position++;

        // Find the first tag token after the current cursor
        // (that way we know everything before that one is just plain text)
        TokenPosition nextPosition = tagPositions.get(this.position);
        while (nextPosition.getOffset() < this.cursor) {
            // If this is the last tag token do nothing.
            if (this.position == (this.tagPositions.size() - 1)) {
                return;
            }

            // Move to the next tag position (because we're still before the current cursor)
            this.position++;
            nextPosition = this.tagPositions.get(this.position);
        }

        // Save everything that's between the current cursor position and the next position
        String tokenText = code.substring(this.cursor, nextPosition.getOffset() - this.cursor);

        /*
        Not sure why this code snippet was here or what it does, find out and check so it's nothing crucial.
        @see Twig_Lexer.php:161-165
        TODO:
            if (isset($this->positions[2][$this->position][0])) {
                $text = rtrim($text);
            }
         */

        // Save found TEXT
        pushToken(Token.Type.TEXT, tokenText);
        // Move past saved text and the next token
        moveCursor(tokenText + nextPosition.getGroup());

        // Check the upcoming tag te see what to lex
        String currentPositionGroup = this.tagPositions.get(this.position).getGroup();
        if (currentPositionGroup.equals(options.getCommentOpen())) {
            pushState(State.COMMENT);

        } else if (currentPositionGroup.equals(options.getBlockOpen())) {
            pushToken(Token.Type.BLOCK_START);
            pushState(State.BLOCK);
            this.currentTagLine = this.line;

        } else if (currentPositionGroup.equals(options.getVariableOpen())) {
            pushToken(Token.Type.VAR_START);
            pushState(State.VARIABLE);
            this.currentTagLine = this.line;
        }
    }

    /**
     * Lexes a variable expression "{{ aVar }}"
     *
     * @throws SyntaxErrorException If we can't keep on tokenizing for some reason (i.e. missing closing tag)
     */
    protected void lexVariable() throws SyntaxErrorException {
        Matcher endVarTagMatcher = this.regexes.getLexVariableEnd().matcher(this.code.substring(this.cursor));

        // Check if this is the variable closing token
        if (this.brackets.size() == 0 && endVarTagMatcher.find(0)) {
            pushToken(Token.Type.VAR_END);
            moveCursor(endVarTagMatcher.group(0));
            popState();
        } else {
            // Parse the var contents (name, operations etc)
            lexExpression();
        }
    }

    protected void lexBlock() throws SyntaxErrorException {
        Matcher endBlockTagMatcher = this.regexes.getLexBlockEnd().matcher(this.code.substring(this.cursor));

        // Check if this is the variable closing token
        if (this.brackets.size() == 0 && endBlockTagMatcher.find(0)) {
            pushToken(Token.Type.BLOCK_END);
            moveCursor(endBlockTagMatcher.group(0));
            popState();
        } else {
            // Parse the block contents (name, strings, operations etc)
            lexExpression();
        }
    }

    /**
     * Does the hard work in lexing all tag contents (ie variable names, operatiors etc)
     *
     * @throws SyntaxErrorException If we can't keep on tokenizing for some reason (i.e. missing closing tag)
     */
    protected void lexExpression() throws SyntaxErrorException {
        // The code to lex
        String codeAfterCursor = this.code.substring(this.cursor);

        // Move past any leading whitespace
        Matcher leadingWhitespacesMatcher = Pattern.compile("^\\s+").matcher(codeAfterCursor);
        if (leadingWhitespacesMatcher.find(0)) {
            moveCursor(leadingWhitespacesMatcher.group(0));

            // Cursor was moved, we need to update codeAfterCursor too
            codeAfterCursor = this.code.substring(this.cursor);

            // We're lexing an expression but we found the end of the template = closing tag is missing
            if (this.cursor >= this.end) {
                throw SyntaxErrorException.unclosedTag(this.state, this.filename, this.currentTagLine);
            }
        }

        if (false) {
            // TODO operators
            return;
        }

        // The name, i.e. var name
        Matcher nameMatcher = this.regexes.getExpressionName().matcher(codeAfterCursor);
        if (nameMatcher.find(0)) {
            pushToken(Token.Type.NAME, nameMatcher.group(0));
            moveCursor(nameMatcher.group(0));

            return;
        }

        // Regular string contents
        Matcher stringMatcher = this.regexes.getExpressionString().matcher(codeAfterCursor);
        if (stringMatcher.find(0)) {
            // Strip quotes from string
            String foundStringContents = stringMatcher.group(0).substring(1, stringMatcher.group(0).length() - 1);
            pushToken(Token.Type.STRING, StringModule.stripcslashes(foundStringContents));
            moveCursor(stringMatcher.group(0));

            return;
        }

        // Opening double quoted string
        Matcher openingStringMatcher = this.regexes.getDoubleQuoteStringDelimiter().matcher(codeAfterCursor);
        if (openingStringMatcher.find(0)) {
            // Increase bracket depth
            this.brackets.add(new Bracket("\"", this.line));
            pushState(State.STRING);
            moveCursor(openingStringMatcher.group(0));

            return;
        }

        // Unlexable
        String unexpectedCharacter = String.valueOf(this.code.charAt(this.cursor));
        throw SyntaxErrorException.unexpectedCharacter(unexpectedCharacter, this.filename, this.line);
    }

    /**
     * Move past comment blocks
     * @throws SyntaxErrorException On any unclosed comments
     */
    protected void lexComment() throws SyntaxErrorException {
        Matcher endCommentTagMatcher = this.regexes.getLexCommentEnd().matcher(this.code.substring(this.cursor));

        // Check if this is the variable closing token
        if (!endCommentTagMatcher.find(0)) {
            throw SyntaxErrorException.unclosedComment(this.filename, this.line);
        }

        moveCursor(this.code.substring(this.cursor, this.cursor + endCommentTagMatcher.end(0)));
        popState();
    }

    protected void lexString() throws SyntaxErrorException {
        String codeAfterCursor = this.code.substring(this.cursor);

        // If this is a string interpolation
        Matcher stringInterpolationStartMatcher = this.regexes.getInterpolationStart().matcher(codeAfterCursor);
        if (stringInterpolationStartMatcher.find(0)) {
            // We're now doing string interpolation
            // Add a depth in brackets
            this.brackets.add(new Bracket(this.options.getInterpolationOpen(), this.line));
            pushToken(Token.Type.INTERPLATION_START);
            moveCursor(stringInterpolationStartMatcher.group(0));
            pushState(State.INTERPOLATION);

            return;
        }

        // If this is just a regular string
        Matcher stringDoubleQuotePartMatcher = this.regexes.getDoubleQuoteStringPart().matcher(codeAfterCursor);
        if (stringDoubleQuotePartMatcher.find(0) && stringDoubleQuotePartMatcher.group(0).length() > 0) {
            // Push the string token
            pushToken(Token.Type.STRING, StringModule.stripcslashes(stringDoubleQuotePartMatcher.group(0)));
            moveCursor(stringDoubleQuotePartMatcher.group(0));

            return;
        }

        // If this is the end of a string
        Matcher stringDoubleQuoteDelimiterMatcher = this.regexes.getDoubleQuoteStringDelimiter().matcher(codeAfterCursor);
        if (stringDoubleQuoteDelimiterMatcher.find(0)) {
            // Move up one bracket level
            Bracket bracket = this.brackets.remove(this.brackets.size() - 1);
            // Check so the cursor is actually at a quote
            if (this.code.charAt(this.cursor) != '"') {
                throw SyntaxErrorException.unclosedSomething(bracket.getType(), this.filename, this.line);
            }

            // Closing of the string
            popState();
            this.cursor ++;
        }
    }

    /**
     * Pushes a token to the TokenStream
     *
     * @param type  The token type
     * @param value The value of the token (or null if no value, ie for start/end tags)
     */
    protected void pushToken(Token.Type type, String value) {
        // Don't push empty TEXT tokens
        if (type == Token.Type.TEXT && value.equals("")) {
            return;
        }

        this.tokenStream.add(new Token(type, value, this.line));
    }

    /**
     * Pushes a token to the token stream without a value
     *
     * @param type The token type
     */
    protected void pushToken(Token.Type type) {
        // Don't push empty TEXT tokens
        if (type == Token.Type.TEXT) {
            return;
        }

        this.tokenStream.add(new Token(type, null, this.line));
    }

    /**
     * Sets a new state (pushes it since states can be nestled)
     *
     * @param state The new state
     */
    protected void pushState(State state) {
        this.states.add(state);
        this.state = state;
    }

    /**
     * Resets the state to the previous state
     */
    protected void popState() {
        if (states.size() == 0) {
            throw new RuntimeException("Cannot pop state without a previous state");
        }

        states.remove(states.size() - 1);

        state = states.get(states.size() - 1);
    }

    /**
     * Moves the cursor and the line number past as many characters/line breaks as the provided text contains
     *
     * @param text The text to move the cursor past
     */
    protected void moveCursor(String text) {
        cursor += text.length();
        this.line += findNumberOfLineEndingsInText(text);
    }

    /**
     * Finds the number of line breaks in the provided text
     *
     * @param text The text to find line breaks in
     * @return The number of line endings
     */
    protected Integer findNumberOfLineEndingsInText(String text) {
        Matcher matcher = Pattern.compile("\n").matcher(text);

        Integer lineEndingsInText = 0;
        Integer matcherPosition = 0;
        while (matcher.find(matcherPosition)) {
            lineEndingsInText++;
            matcherPosition++;
        }

        return lineEndingsInText;
    }

    /**
     * Finds the starts of all tag tokens to tokenize (makes us not having to step through each character
     * of the template)
     */
    protected void findTokenPositions() {
        Matcher matcher = regexes.getLexTokensStart().matcher(this.code);

        while (matcher.find()) {
            this.tagPositions.add(new TokenPosition(matcher.start(), matcher.group()));
        }
    }
}
