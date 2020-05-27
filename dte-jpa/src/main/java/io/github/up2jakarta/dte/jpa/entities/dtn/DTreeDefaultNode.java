package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.dtree.IDefaultNode;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * {@link IDefaultNode} decision node implementation.
 */
@Entity
@DiscriminatorValue(value = "E")
public class DTreeDefaultNode extends DTreeChildNode implements IDefaultNode {

    /**
     * Public constructor for DTreeDefaultNode.
     */
    @SuppressWarnings("deprecation")
    public DTreeDefaultNode() {
        setLabel("Default");
    }
}
