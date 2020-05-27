package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.btree.IMixedDecider;
import io.github.up2jakarta.dte.jpa.api.btree.IOperatorNode;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import java.util.Comparator;
import java.util.Objects;
import java.util.Optional;

/**
 * Abstract {@link IChildNode}.
 */
@Entity
@DiscriminatorValue(value = ":")
public abstract class BTreeChildNode extends BTreeNode implements IChildNode {

    @Column(name = "BTN_ORDER")
    private Integer order;

    /**
     * JPA default constructor for BTreeChildNode.
     */
    protected BTreeChildNode() {
        super(null);
    }

    @Override
    public Integer getOrder() {
        return order;
    }

    @Override
    public void setOrder(final Integer order) {
        this.order = order;
    }

    @Override
    public IOperatorNode getParent() {
        return super.parent;
    }

    @Override
    public void setParent(final IOperatorNode parent) {
        super.parent = parent;
        super.root = Optional.ofNullable(parent).map(IOperatorNode::getRoot).orElse(null);
        super.depth = Optional.ofNullable(parent).map(IOperatorNode::getDepth).map(d -> d + 1).orElse(null);
    }

    @Override
    public IMixedDecider getRoot() {
        return super.root;
    }

    @Override
    public Integer getDepth() {
        return super.depth;
    }

    @Override
    @SuppressWarnings("ALL")
    public int compareTo(final IChildNode other) {
        return Comparator.comparingInt(IChildNode::getDepth)
                .thenComparingInt(IChildNode::getOrder)
                .thenComparing(IChildNode::toString)
                .compare(this, other);
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BTreeChildNode)) {
            return false;
        }
        final BTreeChildNode that = (BTreeChildNode) other;
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
