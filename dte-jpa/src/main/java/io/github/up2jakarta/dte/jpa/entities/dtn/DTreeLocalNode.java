package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.IComputer;
import io.github.up2jakarta.dte.jpa.api.dtree.IComputationNode;
import io.github.up2jakarta.dte.jpa.api.dtree.IPlainComputer;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * {@link IPlainComputer} computer node implementation.
 */
@Entity
@DiscriminatorValue(value = "T")
@SuppressWarnings("unused")
public class DTreeLocalNode extends DTreeScriptNode implements IComputationNode, IPlainComputer<Template> {

    @ElementCollection
    @CollectionTable(
            name = "DTE_DOP",
            joinColumns = @JoinColumn(name = "DOP_DTN_ID", foreignKey = @ForeignKey(name = "DOP_DTN_FK")),
            uniqueConstraints = @UniqueConstraint(name = "DOP_LABEL_UC", columnNames = {"DOP_DTN_ID", "DOP_LABEL"})
    )
    @MapKeyColumn(name = "DOP_NAME", length = 31)
    private Map<String, Output> declaredTyping = new HashMap<>();

    /**
     * JPA default constructor for DTreeLocalNode.
     */
    protected DTreeLocalNode() {
        this(null);
    }

    /**
     * Protected constructor for BTreeLocalNode.
     *
     * @param group the group
     */
    public DTreeLocalNode(final Group group) {
        super(group);
    }

    @Override
    public Map<String, Output> getDeclaredTyping() {
        if (declaredTyping == null) {
            declaredTyping = new HashMap<>();
        }
        return declaredTyping;
    }

    @Override
    public IComputer getComputer() {
        return this;
    }

}
