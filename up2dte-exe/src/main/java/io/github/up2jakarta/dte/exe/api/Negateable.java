package io.github.up2jakarta.dte.exe.api;

import io.github.up2jakarta.dte.exe.api.dtree.DecisionNode;

/**
 * Negate-able interface.
 *
 * @see Decider
 * @see DecisionNode
 */
public interface Negateable {

    /**
     * Return a flag that tells the engine to negate the result of {@link Decider} after evaluation.
     *
     * @return the indicator flag
     */
    boolean isNegated();
}
