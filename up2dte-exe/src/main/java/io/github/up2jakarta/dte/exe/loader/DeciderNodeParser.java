package io.github.up2jakarta.dte.exe.loader;

import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.btree.*;
import io.github.up2jakarta.dte.exe.api.btree.*;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.btree.Leaf;
import io.github.up2jakarta.dte.exe.engine.btree.Node;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;

/**
 * Binary Tree Node parser implementation.
 */
public class DeciderNodeParser implements NodeParser<Node, ChildNode> {

    private static final String ERR_CT_FORMAT = "unknown decider type %s of the node %s";
    private static final String ERR_ST_FORMAT = "unknown decider type %s";

    private final StaticEngine engine;

    /**
     * Protected constructor for ComputerNodeParser.
     *
     * @param engine the DTE engine
     */
    DeciderNodeParser(final StaticEngine engine) {
        this.engine = engine;
    }

    /**
     * Convert a persisted child node to a engine decider node.
     *
     * @param node the child node
     * @return the engine decider node
     */
    @Override
    public Node parse(final ChildNode node) {
        if (node instanceof ParentNode) {
            final ParentNode parent = (ParentNode) node;
            return Operand.of(parent.getOperator(), node.isNegated());
        } else if (node instanceof LeafNode) {
            final Decider decider = ((LeafNode) node).getDecider();
            if (node instanceof LazyDecider) {
                return Leaf.of(Condition.of(engine, decider.getId()), node.isNegated());
            } else if (decider instanceof PlainDecider) {
                final PlainDecider s = (PlainDecider) decider;
                return Leaf.of(Condition.of(s.getTemplate().getBaseClass(), s.getId(), s.getScript()), s.isNegated());
            } else {
                throw new ParsingException(String.format(ERR_CT_FORMAT, decider.getClass(), node));
            }
        } else {
            throw new ParsingException(String.format(ERR_ST_FORMAT, node));
        }
    }
}
