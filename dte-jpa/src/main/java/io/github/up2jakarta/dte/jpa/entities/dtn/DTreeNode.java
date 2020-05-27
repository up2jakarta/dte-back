package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.dtree.INode;
import io.github.up2jakarta.dte.jpa.entities.DocumentedType;
import io.github.up2jakarta.dte.jpa.entities.ShareableType;

import javax.persistence.*;

/**
 * Abstract {@link INode} (DTN).
 */
@Entity
@Table(name = "DTE_DTN", uniqueConstraints = {
        @UniqueConstraint(name = "DTN_LABEL_UC", columnNames = {"DTN_GRP_ID", "DTN_LABEL"}),
        @UniqueConstraint(name = "DTN_ORDER_UC", columnNames = {"DTN_PARENT_ID", "DTN_ORDER"}),
        @UniqueConstraint(name = "DTN_BTN_UC", columnNames = {"DTN_PARENT_ID", "DTN_BTN_ID", "DTN_NEGATED"})
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "DTN_TYPE", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "?")
public abstract class DTreeNode implements INode {

    private static final String SEQUENCE = "DTE_DTN_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, initialValue = 9999, allocationSize = 25)
    @Column(name = "DTN_ID", nullable = false)
    private Long id;

    @Column(name = "DTN_LABEL", nullable = false, length = 63)
    private String label;

    /**
     * JPA default constructor for DTreeNode.
     */
    protected DTreeNode() {
    }

    @Override
    public Long getId() {
        return id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public abstract String toString();


    /**
     * Decision tree typing input.
     */
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "label", column = @Column(name = "DIP_LABEL", nullable = false, length = 63)),
            @AttributeOverride(name = "description", column = @Column(name = "DIP_DESC", nullable = false)),
            @AttributeOverride(name = "optional", column = @Column(name = "DIP_OPTIONAL", nullable = false))
    })
    @AssociationOverride(
            name = "type",
            joinColumns = @JoinColumn(name = "DIP_TPE_ID", nullable = false),
            foreignKey = @ForeignKey(name = "DIP_TPE_FK")
    )
    public static class Input extends DocumentedType {
    }

    /**
     * Decision tree typing output.
     */
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "label", column = @Column(name = "DOP_LABEL", nullable = false, length = 63)),
            @AttributeOverride(name = "description", column = @Column(name = "DOP_DESC", nullable = false)),
            @AttributeOverride(name = "optional", column = @Column(name = "DOP_OPTIONAL", nullable = false)),
            @AttributeOverride(name = "shared", column = @Column(name = "DOP_SHARED", nullable = false))
    })
    @AssociationOverride(
            name = "type",
            joinColumns = @JoinColumn(name = "DOP_TPE_ID", nullable = false),
            foreignKey = @ForeignKey(name = "DOP_TPE_FK")
    )
    public static class Output extends ShareableType {
    }

}
