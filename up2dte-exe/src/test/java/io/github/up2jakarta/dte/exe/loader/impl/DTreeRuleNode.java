package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.dtree.ComputationNode;
import io.github.up2jakarta.dte.exe.api.dtree.LazyComputer;

public class DTreeRuleNode extends DTreeChildNode implements ComputationNode, LazyComputer {

    private final Computer calculation;

    public DTreeRuleNode(final long id, final DTreeNode parent, final int order, final Computer calculation) {
        super(id, parent, order);
        this.calculation = calculation;
    }

    @Override
    public Computer getComputer() {
        return calculation;
    }

}
