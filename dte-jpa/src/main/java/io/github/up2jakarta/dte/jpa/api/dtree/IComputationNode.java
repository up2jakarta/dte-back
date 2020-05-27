package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.dtree.ComputationNode;
import io.github.up2jakarta.dte.jpa.api.IComputer;

/**
 * Decision tree base computer node.
 */
public interface IComputationNode extends IChildNode, IParentNode, ComputationNode {

    @Override
    IComputer getComputer();
}
