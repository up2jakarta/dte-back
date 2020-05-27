package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.api.IComputer;
import io.github.up2jakarta.dte.jpa.api.IGroup;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreeNode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract {@link IComputer} (DTN).
 */
@Entity
@NamedQuery(
        name = "DTN_FIND_BY_ID",
        query = "SELECT r FROM DTreeComputer r"
                + " INNER JOIN FETCH r.group"
                + " LEFT  JOIN FETCH r.nodes n"
                + " LEFT  JOIN FETCH n.decider d"
                + " LEFT  JOIN FETCH d.template"
                + " LEFT  JOIN FETCH n.computer c"
                + " LEFT  JOIN FETCH c.template"
                + " WHERE r.id = :id"
)
//TODO finder no join decider & computer
@DiscriminatorValue(value = ":")
public abstract class DTreeComputer extends DTreeNode implements IComputer {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "DTN_DESC")
    private String description;

    @Column(name = "DTN_SHARED")
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Group.class)
    @JoinColumn(name = "DTN_GRP_ID", foreignKey = @ForeignKey(name = "DTN_GRP_FK"))
    private IGroup group;

    @ElementCollection
    @CollectionTable(
            name = "DTE_DIP",
            joinColumns = @JoinColumn(name = "DIP_DTN_ID", foreignKey = @ForeignKey(name = "DIP_DTN_FK")),
            uniqueConstraints = @UniqueConstraint(name = "DIP_LABEL_UC", columnNames = {"DIP_DTN_ID", "DIP_LABEL"})
    )
    @MapKeyColumn(name = "DIP_NAME", length = 31)
    private Map<String, Input> typing = new HashMap<>();

    @ElementCollection
    @CollectionTable(
            name = "DTE_DOP",
            joinColumns = @JoinColumn(name = "DOP_DTN_ID", foreignKey = @ForeignKey(name = "DOP_DTN_FK")),
            uniqueConstraints = @UniqueConstraint(name = "DOP_LABEL_UC", columnNames = {"DOP_DTN_ID", "DOP_LABEL"})
    )
    @MapKeyColumn(name = "DOP_NAME", length = 31)
    private Map<String, Output> declaredTyping = new HashMap<>();

    /**
     * JPA default constructor for DTreeComputer.
     */
    protected DTreeComputer() {
        this.shared = false;
    }

    /**
     * Protected constructor for DTreeComputer.
     *
     * @param group the group
     */
    protected DTreeComputer(final Group group) {
        this();
        this.group = group;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String desc) {
        this.description = desc;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    @Override
    public void setShared(final boolean shared) {
        this.shared = shared;
    }

    @Override
    public IGroup getGroup() {
        return group;
    }

    @Override
    public Map<String, Input> getTyping() {
        if (typing == null) {
            typing = new HashMap<>();
        }
        return typing;
    }

    @Override
    public Map<String, Output> getDeclaredTyping() {
        if (declaredTyping == null) {
            declaredTyping = new HashMap<>();
        }
        return declaredTyping;
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof DTreeComputer)) {
            return false;
        }
        final DTreeComputer that = (DTreeComputer) other;
        return Objects.equals(getLabel(), that.getLabel()) && Objects.equals(getGroup(), that.getGroup());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getGroup());
    }

    @Override
    public final String toString() {
        return String.valueOf(getGroup()) + '/' + getLabel();
    }
}
