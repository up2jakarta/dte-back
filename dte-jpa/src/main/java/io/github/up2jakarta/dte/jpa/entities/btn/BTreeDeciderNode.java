package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.btree.IDeciderNode;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;

import javax.persistence.*;

/**
 * {@link IDeciderNode} leaf node implementation.
 */
@Entity
@DiscriminatorValue(value = "L")
public class BTreeDeciderNode extends BTreeChildNode implements IDeciderNode {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = BTreeDecider.class)
    @JoinColumn(name = "BTN_BTN_ID", foreignKey = @ForeignKey(name = "BTN_BTN_ID"))
    private IDecider decider;


    @Override
    public IDecider getDecider() {
        return decider;
    }

    @Override
    public void setDecider(final IDecider decider) {
        this.decider = decider;
        setLabel(decider.getLabel());
    }

}
