package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.exe.api.btree.ChildNode;
import io.github.up2jakarta.dte.jpa.api.Node;

/**
 * Binary tree child node.
 */
public interface IChildNode extends Comparable<IChildNode>, INode, ChildNode, Node<IMixedDecider> {

    /**
     * Set the child order in parent node.
     *
     * @param order the order
     */
    void setOrder(Integer order);

    @Override
    IOperatorNode getParent();

    /**
     * Set the parent node to the given one.
     *
     * @param parent the parent node
     */
    void setParent(IOperatorNode parent);

}
