package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.jpa.api.IComputer;

/**
 * Decision tree link node to a shareable computer.
 */
public interface IComputerNode extends IComputationNode {

    /**
     * Set the callee computer whenever simple or complex.
     *
     * @param c the related computer
     */
    void setComputer(IComputer c);
}
