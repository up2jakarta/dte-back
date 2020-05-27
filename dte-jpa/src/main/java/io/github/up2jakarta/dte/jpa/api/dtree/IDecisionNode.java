package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.Negateable;
import io.github.up2jakarta.dte.exe.api.dtree.DecisionNode;
import io.github.up2jakarta.dte.jpa.api.IDecider;

/**
 * Decision tree decision node.
 */
public interface IDecisionNode extends IDefaultNode, Negateable, DecisionNode {

    /**
     * Set the negated flag.
     *
     * @param negated the negated flag
     */
    void setNegated(boolean negated);

    @Override
    IDecider getDecider();

}
