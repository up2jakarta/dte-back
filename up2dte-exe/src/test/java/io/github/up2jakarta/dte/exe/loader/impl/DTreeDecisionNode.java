package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.btree.LazyDecider;
import io.github.up2jakarta.dte.exe.api.dtree.DecisionNode;

public class DTreeDecisionNode extends DTreeChildNode implements DecisionNode, LazyDecider {

    private final boolean negated;
    private final Decider condition;

    public DTreeDecisionNode(final long id, final DTreeNode parent, final int order
            , final Decider condition, final boolean negated) {
        super(id, parent, order);
        this.condition = condition;
        this.negated = negated;
    }

    @Override
    public Decider getDecider() {
        return condition;
    }

    @Override
    public boolean isNegated() {
        return negated;
    }
}
