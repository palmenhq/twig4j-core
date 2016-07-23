package org.twig.syntax;

import org.twig.exception.SyntaxErrorException;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Lexer {
    public static enum State {
        TEXT, BLOCK, VARIABLE, STRING, INTERPOLATION
    }

    /**
     * Represents a token position in the code stream
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

    private String code;
    private String filename;
    private Integer cursor;
    private Integer position;
    private Integer end;
    private Integer line;
    private Integer currentTagLine;
    private State state;
    private List<State> states;
    private List brackets;
    private TokenStream tokenStream;
    private LexerOptions options;
    private LexerRegexes regexes;
    private List<TokenPosition> tagPositions;

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

    public TokenStream tokenize(String code, String filename) throws SyntaxErrorException {
        this.code = code;
        this.filename = filename;
        this.cursor = 0;
        this.end = code.length();
        this.line = 1;
        this.currentTagLine = 1;
        this.tokenStream = new TokenStream();
        this.position = -1;
        this.tagPositions = new ArrayList<>();
        this.brackets = new ArrayList<>();
        this.states = new ArrayList<>();
        pushState(State.TEXT);

        findTokenPositions();

        while (this.cursor < this.end) {
            switch (this.state) {
                case TEXT:
                    lexData();
                    break;
                case VARIABLE:
                    lexVariable();
                    break;
            }
        }

        return this.tokenStream;
    }

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
        TokenPosition nextPosition = tagPositions.get(this.position);
        while (nextPosition.getOffset() < this.cursor) {
            // If this is the last tag token do nothing
            if (this.position == (this.tagPositions.size() - 1)) {
                return;
            }

            // Move to the next tag position (because we're still before the current cursor)
            this.position++;
            nextPosition = this.tagPositions.get(this.position);
        }

        // Push the template text first
        String tokenText = code.substring(this.cursor, nextPosition.getOffset() - this.cursor);
        String textReal = tokenText;
        /*
        TODO:
            if (isset($this->positions[2][$this->position][0])) {
                $text = rtrim($text);
            }
         */

        pushToken(Token.Type.TEXT, tokenText);
        moveCursor(textReal + nextPosition.getGroup());

        String currentPositionGroup = this.tagPositions.get(this.position).getGroup();
        if (currentPositionGroup.equals(options.getCommentOpen())) {
            // TODO lex comment
        } else if (currentPositionGroup.equals(options.getBlockOpen())) {
            // TODO lex block
        } else if (currentPositionGroup.equals(options.getVariableOpen())) {
            pushToken(Token.Type.VAR_START);
            pushState(State.VARIABLE);
            this.currentTagLine = this.line;
        }
    }

    protected void lexVariable() throws SyntaxErrorException {
        Matcher endVarTagMatcher = this.regexes.getLexVariableEnd().matcher(this.code.substring(this.cursor));

        if (this.brackets.size() == 0 && endVarTagMatcher.find(0)) {
            pushToken(Token.Type.VAR_END);
            moveCursor(endVarTagMatcher.group(0));
            popState();
        } else {

            lexExpression();
        }
    }

    protected void lexExpression() throws SyntaxErrorException {
        String codeAfterCursor = this.code.substring(this.cursor);

        // Move past any  leading whitespace
        Matcher leadingWhitespacesMatcher = Pattern.compile("^\\s+").matcher(codeAfterCursor);
        if (leadingWhitespacesMatcher.find(0)) {
            moveCursor(leadingWhitespacesMatcher.group(0));

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
    }

    protected void pushToken(Token.Type type, String value) {
        // Don't push empty text tokens
        if (type == Token.Type.TEXT && value.equals("")) {
            return;
        }

        this.tokenStream.add(new Token(type, value, this.line));
    }

    protected void pushToken(Token.Type type) {
        // Don't push empty text tokens
        if (type == Token.Type.TEXT) {
            return;
        }

        this.tokenStream.add(new Token(type, null, this.line));
    }

    protected void pushState(State state) {
        this.states.add(state);
        this.state = state;
    }

    protected void popState() {
        if (states.size() == 0) {
            throw new RuntimeException("Cannot pop state without a previous state");
        }

        states.remove(states.size() - 1);

        state = states.get(states.size() - 1);
    }

    protected void moveCursor(String text) {
        cursor += text.length();
        this.line += findNumberOfLineEndingsInText(text);
    }

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

    protected void findTokenPositions() {
        Matcher matcher = regexes.getLexTokensStart().matcher(this.code);

        while (matcher.find()) {
            this.tagPositions.add(new TokenPosition(matcher.start(), matcher.group()));
        }
    }
}
