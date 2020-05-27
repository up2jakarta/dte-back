package io.github.up2jakarta.dte.exe.loader;

import groovy.lang.Script;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.btree.LazyDecider;
import io.github.up2jakarta.dte.exe.api.btree.PlainDecider;
import io.github.up2jakarta.dte.exe.api.dtree.*;
import io.github.up2jakarta.dte.exe.api.dtree.*;
import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.Decision;
import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;

/**
 * Decision Tree Node parser implementation.
 */
public class ComputerNodeParser implements NodeParser<Node, ChildNode> {

    private static final String ERR_RT_FORMAT = "unknown computer type %s of the node %s";
    private static final String ERR_CT_FORMAT = "unknown decider type %s of the node %s";
    private static final String ERR_ST_FORMAT = "unknown computer type %s";

    private final StaticEngine engine;

    /**
     * Protected constructor for ComputerNodeParser.
     *
     * @param engine the DTE engine
     */
    ComputerNodeParser(final StaticEngine engine) {
        this.engine = engine;
    }

    /**
     * Convert a persisted child node to a engine computer node.
     *
     * @param node the persisted node
     * @return the engine computer node
     */
    @Override
    public Node parse(final ChildNode node) {
        if (node instanceof DefaultNode) {
            if (node instanceof DecisionNode) {
                final DecisionNode decision = (DecisionNode) node;
                if (node instanceof LazyDecider) {
                    final Decider lazy = ((LazyDecider) decision).getDecider();
                    return Decision.of(Condition.of(engine, lazy.getId()), decision.isNegated());
                } else if (decision.getDecider() instanceof PlainDecider) {
                    final PlainDecider plain = (PlainDecider) decision.getDecider();
                    final Class<? extends Script> base = plain.getTemplate().getBaseClass();
                    return Decision.of(Condition.of(base, plain.getId(), plain.getScript()), decision.isNegated());
                }
                throw new ParsingException(String.format(ERR_CT_FORMAT, decision.getDecider().getClass(), node));
            }
            return Decision.empty();
        } else if (node instanceof ComputationNode) {
            final Computer rule = ((ComputationNode) node).getComputer();
            if (node instanceof LazyComputer) {
                return Rule.of(Calculation.of(engine, rule.getId()));
            } else if (rule instanceof PlainComputer) {
                final PlainComputer plain = (PlainComputer) rule;
                return Rule.of(Calculation.of(plain.getTemplate().getBaseClass(), plain.getId(), plain.getScript()));
            }
            throw new ParsingException(String.format(ERR_RT_FORMAT, node.getClass(), node));
        }
        throw new ParsingException(String.format(ERR_ST_FORMAT, node));
    }
}
