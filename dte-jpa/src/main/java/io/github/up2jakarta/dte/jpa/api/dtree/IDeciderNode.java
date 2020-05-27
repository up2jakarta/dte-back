package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.jpa.api.IDecider;

/**
 * Decision tree decision node based on a link to a shareable decider.
 */
public interface IDeciderNode extends IDecisionNode {

    /**
     * Set the related shareable decider.
     *
     * @param decider the shareable decider
     */
    void setDecider(IDecider decider);

}
