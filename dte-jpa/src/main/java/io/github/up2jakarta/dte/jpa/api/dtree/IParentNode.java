package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.jpa.api.Node;

import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.stream.Collectors;

/**
 * Decision tree parent node.
 */
public interface IParentNode extends Identifiable<Long>, Node<IMixedComputer> {

    /**
     * @return the depth in the tree
     */
    Integer getDepth();

    /**
     * The direct children nodes.
     *
     * @return the children nodes
     */
    Set<IChildNode> getChildren();

    /**
     * Find the direct children nodes in root {@link IMixedComputer#getNodes()} by identifier and return them.
     *
     * @return the children nodes
     */
    default SortedSet<IChildNode> findChildren() {
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
    default void addChild(IChildNode child, final int order) {
        if (child != null) {
            child.setParent(this);
            child.setOrder(order);
            getChildren().add(child);
            getRoot().getNodes().add(child);
        }
    }

    /**
     * Remove an existing child node.
     *
     * @param child the decision to add
     */
    default void removeChild(IChildNode child) {
        if (child != null) {
            getRoot().getNodes().remove(child);
            getChildren().remove(child);
            child.setParent(null);
            child.setOrder(null);
        }
    }

}
