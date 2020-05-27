package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.api.btree.ParentNode;
import io.github.up2jakarta.dte.jpa.api.Node;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Binary tree operator node.
 */
public interface IOperatorNode extends INode, ParentNode, Node<IMixedDecider> {

    /**
     * @return the depth in the tree
     */
    Integer getDepth();

    /**
     * Set the operator.
     *
     * @param operator the operator
     */
    void setOperator(Operator operator);

    /**
     * The direct operand nodes.
     *
     * @return the operand nodes
     */
    Set<IChildNode> getOperands();

    /**
     * Find the direct operand nodes in root {@link IMixedDecider#getNodes()} by identifier and return them.
     *
     * @return the operand nodes
     */
    default SortedSet<IChildNode> findOperands() {
        return getRoot().getNodes().stream()
                .filter(n -> getId().equals(n.getParent().getId()))
                .collect(Collectors.toCollection(TreeSet::new));
    }

    /**
     * Add a new child node if does not exists.
     *
     * @param child the child to add
     * @param order the child order
     */
    default void addOperand(IChildNode child, final int order) {
        if (child != null) {
            child.setParent(this);
            child.setOrder(order);
            getOperands().add(child);
            getRoot().getNodes().add(child);
        }
    }

    /**
     * Remove an existing child node.
     *
     * @param child the child to add
     */
    default void removeOperand(IChildNode child) {
        if (child != null) {
            getRoot().getNodes().remove(child);
            getOperands().remove(child);
            child.setParent(null);
            child.setOrder(null);
        }
    }
}
