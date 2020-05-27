package io.github.up2jakarta.dte.exe.loader.impl;


import io.github.up2jakarta.dte.exe.api.dtree.MixedComputer;

import java.util.SortedSet;
import java.util.TreeSet;

public class DTreeMixedCalculation extends DTreeCalculation implements MixedComputer {

    private final String label;
    private final SortedSet<DTreeChildNode> nodes = new TreeSet<>();

    public DTreeMixedCalculation(final long id, final String label) {
        super(id);
        this.label = label;
    }

    @Override
    public SortedSet<DTreeChildNode> getNodes() {
        return nodes;
    }

    @Override
    public String getLabel() {
        return label;
    }

}
