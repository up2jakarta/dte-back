package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.btree.IPlainDecider;
import io.github.up2jakarta.dte.jpa.api.dtree.IDecisionNode;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * {@link IPlainDecider} decision node implementation.
 */
@Entity
@DiscriminatorValue(value = "B")
@SuppressWarnings("unused")
public class DTreeDecisionNode extends DTreeScriptNode implements IDecisionNode, IPlainDecider<Template> {

    @Column(name = "DTN_NEGATED")
    private Boolean negated;

    /**
     * JPA default constructor for DTreeDecisionNode.
     */
    protected DTreeDecisionNode() {
        this(null);
    }

    /**
     * Protected constructor for DTreeDecisionNode.
     *
     * @param group the group
     */
    public DTreeDecisionNode(final Group group) {
        super(group);
        negated = false;
    }

    @Override
    public boolean isNegated() {
        return negated;
    }

    @Override
    public void setNegated(final boolean negated) {
        this.negated = negated;
    }

    @Override
    public IDecider getDecider() {
        return this;
    }

}
