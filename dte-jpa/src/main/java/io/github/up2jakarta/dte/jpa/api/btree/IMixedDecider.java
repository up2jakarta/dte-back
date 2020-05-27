package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.exe.api.btree.MixedDecider;
import io.github.up2jakarta.dte.jpa.api.IDecider;

import java.util.SortedSet;

/**
 * Binary tree mixed node, root node of an hierarchical decider.
 */
public interface IMixedDecider extends IDecider, IOperatorNode, MixedDecider {

    @Override
    SortedSet<IChildNode> getNodes();

}
