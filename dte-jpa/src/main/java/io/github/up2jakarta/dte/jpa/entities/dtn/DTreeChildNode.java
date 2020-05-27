package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.dtree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.dtree.IMixedComputer;
import io.github.up2jakarta.dte.jpa.api.dtree.IParentNode;

import javax.persistence.*;
import java.util.*;

import static javax.persistence.CascadeType.ALL;

/**
 * Abstract {@link IChildNode}.
 */
@Entity
@DiscriminatorValue(value = "!")
@SuppressWarnings("DeprecatedIsStillUsed")
public abstract class DTreeChildNode extends DTreeNode implements IChildNode {

    @Column(name = "DTN_ORDER")
    private Integer order;

    @Column(name = "DTN_DEPTH")
    private Integer depth;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DTreeMixedComputer.class)
    @JoinColumn(name = "DTN_ROOT_ID", foreignKey = @ForeignKey(name = "DTN_ROOT_FK"))
    private IMixedComputer root;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DTreeNode.class)
    @JoinColumn(name = "DTN_PARENT_ID", foreignKey = @ForeignKey(name = "DTN_PARENT_FK"))
    private IParentNode parent;

    @OneToMany(mappedBy = "parent", targetEntity = DTreeChildNode.class, cascade = ALL, orphanRemoval = true)
    private Set<IChildNode> children = new HashSet<>();

    /**
     * JPA default constructor for DTreeChildNode.
     */
    @Deprecated
    DTreeChildNode() {
    }

    @Override
    public IMixedComputer getRoot() {
        return root;
    }

    @Override
    public Integer getDepth() {
        return depth;
    }

    @Override
    public Set<IChildNode> getChildren() {
        return children;
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
    public IParentNode getParent() {
        return parent;
    }

    @Override
    public void setParent(final IParentNode parent) {
        this.parent = parent;
        this.root = Optional.ofNullable(parent).map(IParentNode::getRoot).orElse(null);
        this.depth = Optional.ofNullable(parent).map(IParentNode::getDepth).map(d -> d + 1).orElse(null);
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
