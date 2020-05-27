package io.github.up2jakarta.dte.exe.api.btree;

import io.github.up2jakarta.dte.exe.api.Decider;

import java.util.SortedSet;

/**
 * BTree Mixed Decider, based on hierarchical nodes.
 */
public interface MixedDecider extends Decider, ParentNode {

    /**
     * @return the tree name.
     */
    String getLabel();

    /**
     * The tree nodes must be ordered by depth and order ascending.
     *
     * @return all nodes of this binary tree
     */
    SortedSet<? extends ChildNode> getNodes();

}
