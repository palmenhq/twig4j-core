package org.twigjava.syntax.parser;

import org.twigjava.Environment;
import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.Token;
import org.twigjava.syntax.TokenStream;
import org.twigjava.syntax.parser.node.Module;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.Output;
import org.twigjava.syntax.parser.node.type.Block;
import org.twigjava.syntax.parser.node.type.BlockReference;
import org.twigjava.syntax.parser.node.type.PrintExpression;
import org.twigjava.syntax.parser.node.type.Text;
import org.twigjava.syntax.parser.node.type.expression.Expression;
import org.twigjava.syntax.parser.tokenparser.AbstractTokenParser;

import java.util.*;
import java.util.function.Function;

public class Parser {
    // The token stream to parse
    private TokenStream tokenStream;
    // The expression parser
    private ExpressionParser expressionParser = new ExpressionParser(this);
    // The Twig environment
    private Environment environment;
    // Map of handlers/token parsers
    private HashMap<String, AbstractTokenParser> handlers = new HashMap<>();
    // Parent template name (if any)
    private Expression parent;
    // Blocks
    private Map<String, Block> blocks = new HashMap<>();
    // Handles blocks inside blocks
    private Stack<String> blockStack = new Stack<>();

    public Parser(Environment environment) {
        this.environment = environment;
    }

    /**
     * Parses a template
     *
     * @param tokenStream The token stream to parse
     * @return The Module node (which represents a twigjava file)
     * @throws SyntaxErrorException
     */
    public Module parse(TokenStream tokenStream) throws SyntaxErrorException, TwigRuntimeException {
        // TODO: Find out wth line 64-82 does

        this.handlers.putAll(environment.getTokenParsers());
        this.handlers.forEach((String key, AbstractTokenParser tokenParser) -> tokenParser.setParser(this));

        // TODO: Create node visitors

        this.tokenStream = tokenStream;
        this.parent = null;
        this.blocks = new HashMap<>();
        this.blockStack = new Stack<>();

        // TODO Find out what all the other properties does

        Node body;
        try {
            body = subparse();

            if (this.parent != null) {
                Node filteredBodyNodes = filterBodyNodes(body);

                if (filteredBodyNodes == null) {
                    body = new Node(1);
                }
            }
        } catch (SyntaxErrorException e) {
            if (e.getTemplateName() == null) {
                e.setTemplateName(getFilename());
            }

            if (e.getLineNumber() == null) {
                e.setLineNumber(tokenStream.getCurrent().getLine());
            }

            throw e;
        }

        Module node = new Module(body, parent, this.blocks, tokenStream.getFilename());

        return node;
    }

    /**
     * Does the hard work parsing, but defaults test to null and dropNeedle to false
     * @return The parsed node
     * @throws SyntaxErrorException On syntax errors
     * @throws TwigRuntimeException On any other errors
     */
    public Node subparse() throws SyntaxErrorException, TwigRuntimeException {
        return subparse(null, null, false);
    }

    /**
     * Does the hard work parsing
     *
     * @param test Function that checks for end tags
     * @param subparserTag The name of the token parser calling this method, or null
     * @param dropNeedle Nexts if the `test` function is applied
     * @return The body nodes
     * @throws SyntaxErrorException On syntax errors
     * @throws TwigRuntimeException On any other errors
     */
    public Node subparse(Function<Token, Boolean> test, String subparserTag, Boolean dropNeedle) throws SyntaxErrorException, TwigRuntimeException {
        Integer lineno = tokenStream.getCurrent().getLine();
        ArrayList<Node> rv = new ArrayList<>();

        while (!tokenStream.isEOF()) {
            switch (tokenStream.getCurrent().getType()) {
                case TEXT:
                    Token textToken = tokenStream.next();

                    // Create attribute
                    HashMap<String, Object> attributes = new HashMap<>();
                    attributes.put("data", textToken.getValue());

                    rv.add(new Text(new ArrayList<Node>(), attributes, textToken.getLine(), null));
                    break;

                case VAR_START:
                    Token varStartToken = tokenStream.next();
                    Node expr = expressionParser.parseExpression();
                    tokenStream.expect(Token.Type.VAR_END);
                    rv.add(new PrintExpression(expr, varStartToken.getLine()));
                    break;

                case BLOCK_START:
                    tokenStream.next();
                    Token currentToken = tokenStream.getCurrent();

                    if (!currentToken.is(Token.Type.NAME)) {
                        throw new SyntaxErrorException("A block must start with a tag name.", tokenStream.getFilename(), currentToken.getLine());
                    }

                    if (test != null && test.apply(currentToken)) {
                        if (dropNeedle) {
                            tokenStream.next();
                        }

                        if (rv.size() == 1) {
                            return rv.get(0);
                        }

                        return new Node(rv, new HashMap<>(), lineno, null);
                    }

                    AbstractTokenParser subparser = handlers.get(currentToken.getValue());
                    // Whether a handler/subparser for this tag is found
                    if (subparser == null) {
                        // If we're not testing for end tag
                        if (test == null) {
                            String message = String.format("Unexpected tag name \"%s\"", currentToken.getValue());
                            // If the provided test was part of a calling subparser
                            if (subparserTag != null) {
                                message = message + String.format(" (expecting closing tag for the \"%s\" tag defined near line %s)", subparserTag, lineno);
                            }

                            throw new SyntaxErrorException(message, getFilename(), currentToken.getLine());
                        }
                    }

                    tokenStream.next();

                    Node node = subparser.parse(currentToken);

                    if (node != null) {
                        rv.add(node);
                    }
                    break;

                case EOF:
                    break;

                default:
                    throw new SyntaxErrorException(
                            "Lexer or parser ended up in unsupported state.",
                            tokenStream.getFilename(),
                            tokenStream.getCurrent().getLine()
                    );
            }
        }

        if (rv.size() == 1) {
            return rv.get(0);
        }

        return new Node(rv, new HashMap<>(), lineno, null);
    }

    protected Node filterBodyNodes(Node body) throws SyntaxErrorException, TwigRuntimeException {
        // Check that the body does not contain non-empty output nodes
        // TODO check for file "bom"
        if ((body instanceof Output && !(body instanceof BlockReference) && !(body instanceof Text)) || (body instanceof Text && !((String)body.getAttribute("data")).matches("^[\\s\\n]+$"))) {
            throw new SyntaxErrorException("A template that extends another one cannot have a body.", getFilename(), body.getLine());
        }

        // TODO the thing about "set" nodes

        // Remove outputs
        if (body instanceof Output) {
            return null;
        }

        // Recursively filter out output nodes
        List<Node> newBodyNodes = new ArrayList<>();
        for (Node node : body.getNodes()) {
            Node filteredNode = filterBodyNodes(node);
            if (filteredNode != null) {
                newBodyNodes.add(filteredNode);
            }
        }
        body.getNodes().clear();
        body.getNodes().addAll(newBodyNodes);

        return body;
    }

    /**
     * Get the current token the token stream points to
     *
     * @return The token
     */
    public Token getCurrentToken() {
        return tokenStream.getCurrent();
    }

    /**
     * Check whether we're in the global scope
     *
     * @return
     */
    public Boolean isMainScope() {
        // TODO check something here
        return true;
    }

    /**
     * Get the file name of the token stream currently being parsed
     *
     * @return The file/template name
     */
    public String getFilename() {
        return tokenStream.getFilename();
    }

    public TokenStream getTokenStream() {
        return tokenStream;
    }

    public Parser setTokenStream(TokenStream tokenStream) {
        this.tokenStream = tokenStream;

        return this;
    }

    public ExpressionParser getExpressionParser() {
        return expressionParser;
    }

    public Parser setExpressionParser(ExpressionParser expressionParser) {
        this.expressionParser = expressionParser;

        return this;
    }

    public Environment getEnvironment() {
        return environment;
    }

    public Parser setEnvironment(Environment environment) {
        this.environment = environment;

        return this;
    }

    /**
     * Add a token handler/parser
     *
     * @param key The name (ie "if")
     * @param handler The token parser
     * @return this
     */
    public Parser addHandler(String key, AbstractTokenParser handler) {
        handlers.put(key, handler);

        return this;
    }

    public Parser setHandlers(HashMap<String, AbstractTokenParser> handlers) {
        this.handlers = handlers;

        return this;
    }

    /**
     * Get the parent template name
     *
     * @return
     */
    public Expression getParent() {
        return parent;
    }

    /**
     * Set the parent template name
     *
     * @param parent The parent template name
     * @return this
     */
    public Parser setParent(Expression parent) {
        this.parent = parent;

        return this;
    }

    /**
     * Get the blocks in the current template
     * @return
     */
    public Map<String, Block> getBlocks() {
        return blocks;
    }

    /**
     * Set all blocks in the current template
     *
     * @param blocks Blocks to set
     * @return this
     */
    public Parser setBlocks(Map<String, Block> blocks) {
        this.blocks = blocks;
        return this;
    }

    /**
     * Get the block stack
     *
     * @return The block stack
     */
    public Stack<String> getBlockStack() {
        return blockStack;
    }

    /**
     * Set the whole block stack
     *
     * @param blockStack
     * @return
     */
    public Parser setBlockStack(Stack<String> blockStack) {
        this.blockStack = blockStack;
        return this;
    }
}
