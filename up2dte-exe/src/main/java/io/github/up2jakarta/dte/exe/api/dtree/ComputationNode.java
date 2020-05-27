package io.github.up2jakarta.dte.exe.api.dtree;

import io.github.up2jakarta.dte.exe.api.Computer;

/**
 * DTree Computation Node.
 */
public interface ComputationNode extends ChildNode {

    /**
     * Get the callee computer whenever simple or complex.
     *
     * @return the related computer
     */
    Computer getComputer();
}
