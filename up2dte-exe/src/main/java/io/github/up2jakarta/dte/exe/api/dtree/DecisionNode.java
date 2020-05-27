package io.github.up2jakarta.dte.exe.api.dtree;

import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Negateable;

/**
 * DTree Decision Node.
 */
public interface DecisionNode extends DefaultNode, Negateable {

    /**
     * Get the callee decider whenever simple or complex.
     *
     * @return the related decider
     */
    Decider getDecider();

}
