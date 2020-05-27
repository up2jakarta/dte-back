package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.btree.LazyDecider;
import io.github.up2jakarta.dte.exe.api.btree.LeafNode;
import io.github.up2jakarta.dte.exe.api.btree.ParentNode;

public class BTreeLinkNode extends BTreeChildNode implements LeafNode, LazyDecider {

    private final Decider condition;

    public BTreeLinkNode(final long id, final ParentNode parent, final int order,
                         final Decider condition, final boolean negated) {
        super(id, parent, order, negated);
        this.condition = condition;
    }

    @Override
    public Decider getDecider() {
        return condition;
    }

}
