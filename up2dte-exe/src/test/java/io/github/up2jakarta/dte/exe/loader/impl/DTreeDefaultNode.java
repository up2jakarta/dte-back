package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.dtree.DefaultNode;

public class DTreeDefaultNode extends DTreeChildNode implements DefaultNode {

    public DTreeDefaultNode(final long id, final DTreeNode parent, final int order) {
        super(id, parent, order);
    }
}
