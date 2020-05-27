package io.github.up2jakarta.dte.exe.loader.impl;

import io.github.up2jakarta.dte.exe.api.dtree.ChildNode;

import java.util.Comparator;
import java.util.Objects;
import java.util.function.Function;

public abstract class DTreeChildNode extends DTreeNode implements ChildNode, Comparable<ChildNode> {

    private final int order;
    private final DTreeNode parent;

    DTreeChildNode(final long id, final DTreeNode parent, final int order) {
        super(id);
        this.parent = parent;
        this.order = order;
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public Integer getDepth() {
        throw new UnsupportedOperationException("Not implemented");
    }

    @Override
    public DTreeNode getParent() {
        return parent;
    }

    @Override
    @SuppressWarnings("ALL")
    public int compareTo(final ChildNode other) {
        final Function<ChildNode, Long> parentId = t -> t.getParent().getId();
        return Comparator.comparing(parentId)
                .thenComparingInt(ChildNode::getOrder)
                .compare(this, other);
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DTreeChildNode)) {
            return false;
        }
        final DTreeChildNode that = (DTreeChildNode) other;
        return Objects.equals(getOrder(), that.getOrder()) && Objects.equals(getParent(), that.getParent());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getDepth());
    }

    @Override
    public final String toString() {
        return String.valueOf(getParent()) + '/' + getOrder();
    }

}
