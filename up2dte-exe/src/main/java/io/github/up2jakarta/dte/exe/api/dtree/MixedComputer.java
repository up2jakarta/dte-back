package io.github.up2jakarta.dte.exe.api.dtree;

import io.github.up2jakarta.dte.exe.api.Computer;

import java.util.SortedSet;

/**
 * DTree Mixed Computer, based on hierarchical nodes.
 */
public interface MixedComputer extends Computer {

    /**
     * @return the tree name.
     */
    String getLabel();

    /**
     * The tree nodes must be ordered by depth and order ascending.
     *
     * @return all nodes of this decision tree
     */
    SortedSet<? extends ChildNode> getNodes();

}
