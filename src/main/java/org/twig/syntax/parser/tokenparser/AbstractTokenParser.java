package org.twig.syntax.parser.tokenparser;

import org.twig.exception.SyntaxErrorException;
import org.twig.exception.TwigRuntimeException;
import org.twig.syntax.Token;
import org.twig.syntax.parser.Parser;
import org.twig.syntax.parser.node.Node;

abstract public class AbstractTokenParser {
    protected Parser parser;

    /**
     * Subparse something
     *
     * @param token The token to parse
     * @return The parsed node
     */
    abstract public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException;

    /**
     * Get the tag of the block (ie "if")
     *
     * @return The tag/name
     */
    abstract public String getTag();

    /**
     * Get the main parser
     *
     * @return The main parser
     */
    public Parser getParser() {
        return parser;
    }

    /**
     * Set the main parser
     *
     * @param parser The parser
     * @return this
     */
    public AbstractTokenParser setParser(Parser parser) {
        this.parser = parser;

        return this;
    }
}
