package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.dtree.IDeciderNode;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;

import javax.persistence.*;

/**
 * {@link IDeciderNode} decision node implementation.
 */
@Entity
@DiscriminatorValue(value = "D")
public class DTreeDeciderNode extends DTreeChildNode implements IDeciderNode {

    @Column(name = "DTN_NEGATED")
    private Boolean negated;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = BTreeDecider.class)
    @JoinColumn(name = "DTN_BTN_ID", foreignKey = @ForeignKey(name = "DTN_BTN_ID"))
    private IDecider decider;

    /**
     * Public constructor for DTreeDeciderNode.
     */
    @SuppressWarnings("deprecation")
    public DTreeDeciderNode() {
        this.negated = false;
    }

    @Override
    public IDecider getDecider() {
        return decider;
    }

    @Override
    public void setDecider(final IDecider decider) {
        this.decider = decider;
        setLabel(decider.getLabel());
    }

    @Override
    public boolean isNegated() {
        return negated;
    }

    @Override
    public void setNegated(final boolean negated) {
        this.negated = negated;
    }
}
