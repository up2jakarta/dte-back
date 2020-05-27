package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.IGroup;
import io.github.up2jakarta.dte.jpa.entities.btn.BTreeNode;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Abstract {@link IDecider} (BTN).
 */
@Entity
@NamedQuery(
        name = "BTN_FIND_BY_ID",
        query = "SELECT c FROM BTreeDecider c"
                + " INNER JOIN FETCH c.group"
                + " LEFT JOIN FETCH c.nodes n"
                + " LEFT JOIN FETCH n.decider d"
                + " LEFT JOIN FETCH d.template"
                + " WHERE c.id = :id"
)
@DiscriminatorValue(value = "!")
//TODO finder no join decider
public abstract class BTreeDecider extends BTreeNode implements IDecider {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "BTN_DESC")
    private String description;

    @Column(name = "BTN_SHARED")
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Group.class)
    @JoinColumn(name = "BTN_GRP_ID", foreignKey = @ForeignKey(name = "BTN_GRP_FK"))
    private IGroup group;

    @ElementCollection
    @CollectionTable(
            name = "DTE_BIP",
            joinColumns = @JoinColumn(name = "BIP_BTN_ID", foreignKey = @ForeignKey(name = "BIP_BTN_ID")),
            uniqueConstraints = @UniqueConstraint(name = "BIP_LABEL_UC", columnNames = {"BIP_BTN_ID", "BIP_LABEL"})
    )
    @MapKeyColumn(name = "BIP_NAME", length = 31)
    private final Map<String, Input> typing = new HashMap<>();

    /**
     * JPA default constructor for DTreeComputer.
     */
    protected BTreeDecider() {
        this(null, null);
    }

    /**
     * Protected constructor for DTreeComputer.
     *
     * @param group the group
     * @param depth the node depth
     */
    protected BTreeDecider(final Group group, final Integer depth) {
        super(depth);
        this.group = group;
        this.shared = false;
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
    public IGroup getGroup() {
        return group;
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
    public Map<String, Input> getTyping() {
        return typing;
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BTreeDecider)) {
            return false;
        }
        final BTreeDecider that = (BTreeDecider) other;
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
