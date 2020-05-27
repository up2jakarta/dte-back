package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.dtree.MixedComputer;
import io.github.up2jakarta.dte.jpa.api.IComputer;

import java.util.SortedSet;

/**
 * Decision tree mixed node, root node of an hierarchical computer.
 */
public interface IMixedComputer extends IComputer, IParentNode, MixedComputer {

    @Override
    SortedSet<IChildNode> getNodes();

    @Override
    default IMixedComputer getRoot() {
        return this;
    }
}
