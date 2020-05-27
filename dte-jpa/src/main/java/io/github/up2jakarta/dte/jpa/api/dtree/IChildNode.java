package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.dtree.ChildNode;
import io.github.up2jakarta.dte.jpa.api.Node;

/**
 * Decision tree child node.
 */
public interface IChildNode extends IParentNode, Comparable<IChildNode>, ChildNode, Node<IMixedComputer> {

    /**
     * Set the child order in parent node.
     *
     * @param order the order
     */
    void setOrder(Integer order);

    @Override
    IParentNode getParent();

    /**
     * Set the parent node to the given one.
     *
     * @param parent the parent node
     */
    void setParent(IParentNode parent);

}
