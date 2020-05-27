package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.api.btree.MixedDecider;

import java.util.SortedSet;
import java.util.TreeSet;

public class BTreeMixedCondition extends BTreeCondition implements MixedDecider {

    private final String label;
    private final Operator operator;
    private final SortedSet<BTreeChildNode> nodes = new TreeSet<>();

    public BTreeMixedCondition(final long id, final String label, final Operator operator, final boolean negated) {
        super(id, negated);
        this.operator = operator;
        this.label = label;
    }

    @Override
    public SortedSet<BTreeChildNode> getNodes() {
        return nodes;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public String getLabel() {
        return label;
    }
}
