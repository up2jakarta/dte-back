package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.jpa.api.IDecider;

/**
 * Binary tree link node to a shareable decider.
 */
public interface IDeciderNode extends ILeafNode {

    /**
     * Set the related real decider.
     *
     * @param decider the real decider
     */
    void setDecider(IDecider decider);

}
