package org.twigjava.syntax.parser.tokenparser;

import org.twigjava.exception.SyntaxErrorException;
import org.twigjava.exception.TwigRuntimeException;
import org.twigjava.syntax.Token;
import org.twigjava.syntax.TokenStream;
import org.twigjava.syntax.parser.node.Node;
import org.twigjava.syntax.parser.node.type.BlockReference;
import org.twigjava.syntax.parser.node.type.PrintExpression;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Block extends AbstractTokenParser {
    @Override
    public Node parse(Token token) throws SyntaxErrorException, TwigRuntimeException {
        Integer line = token.getLine();
        TokenStream tokenStream = parser.getTokenStream();
        String blockName = tokenStream.expect(Token.Type.NAME).getValue();
        Node body;

        // A block can only be defined once
        if (parser.getBlocks().containsKey(blockName)) {
            throw new SyntaxErrorException(
                String.format("The block '%s' has already been defined line %d.", blockName, parser.getBlocks().get(blockName).getLine()),
                parser.getFilename(),
                token.getLine()
            );
        }

        org.twigjava.syntax.parser.node.type.Block block = new org.twigjava.syntax.parser.node.type.Block(blockName, new Node(token.getLine()), token.getLine(), getTag());
        parser.getBlocks().put(blockName, block);
        // TODO parser.pushLocalScope()
        parser.getBlockStack().push(blockName);

        if (tokenStream.getCurrent().is(Token.Type.BLOCK_END)) {
            tokenStream.next();

            body = parser.subparse(this::decideIfBlockEnd, getTag(), true);

            // If the endblock tag is with the block name
            if (tokenStream.getCurrent().is(Token.Type.NAME)) {
                String endBlockName = tokenStream.next().getValue();

                if (!endBlockName.equals(blockName)) {
                    throw new SyntaxErrorException(
                        String.format("Expected endblock for block \"%s\" (but \"%s\" given).", blockName, endBlockName)
                    );
                }
            }
        } else {
            List<Node> prints = new ArrayList<>();
            prints.add(new PrintExpression(parser.getExpressionParser().parseExpression(), line));
            body = new Node(prints, new HashMap<>(), line, null);
        }
        tokenStream.expect(Token.Type.BLOCK_END);

        block.getNodes().set(0, body);

        parser.getBlockStack().pop();
        // TODO parser.popLocalScope()

        return new BlockReference(blockName, line, getTag());
    }

    public Boolean decideIfBlockEnd(Token token) {
        return token.test(Token.Type.NAME, "endblock");
    }

    @Override
    public String getTag() {
        return "block";
    }
}
