package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.jpa.api.btree.IMixedDecider;
import io.github.up2jakarta.dte.jpa.api.btree.INode;
import io.github.up2jakarta.dte.jpa.api.btree.IOperatorNode;
import io.github.up2jakarta.dte.jpa.entities.DocumentedType;

import javax.persistence.*;

/**
 * Abstract {@link INode} (BTN).
 */
@Entity
@Table(name = "DTE_BTN", uniqueConstraints = {
        @UniqueConstraint(name = "BTN_LABEL_UC", columnNames = {"BTN_GRP_ID", "BTN_LABEL"}),
        @UniqueConstraint(name = "BTN_ORDER_UC", columnNames = {"BTN_PARENT_ID", "BTN_ORDER"}),
        @UniqueConstraint(name = "BTN_BTN_UC", columnNames = {"BTN_PARENT_ID", "BTN_BTN_ID", "BTN_NEGATED"})
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "BTN_TYPE", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "?")
public abstract class BTreeNode implements INode {

    private static final String SEQUENCE = "DTE_BTN_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, initialValue = 9999, allocationSize = 25)
    @Column(name = "BTN_ID", nullable = false)
    private Long id;

    @Column(name = "BTN_LABEL", nullable = false, length = 63)
    private String label;

    @Column(name = "BTN_NEGATED", nullable = false)
    private Boolean negated;

    @Column(name = "BTN_DEPTH")
    protected Integer depth;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = BTreeMixedDecider.class)
    @JoinColumn(name = "BTN_ROOT_ID", foreignKey = @ForeignKey(name = "BTN_ROOT_FK"))
    protected IMixedDecider root;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = BTreeNode.class)
    @JoinColumn(name = "BTN_PARENT_ID", foreignKey = @ForeignKey(name = "BTN_PARENT_FK"))
    protected IOperatorNode parent;

    /**
     * Protected constructor for BTreeNode.
     *
     * @param depth the depth in the root tree
     */
    protected BTreeNode(final Integer depth) {
        this.depth = depth;
        this.negated = false;
    }

    @Override
    public Long getId() {
        return id;
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
            @AttributeOverride(name = "label", column = @Column(name = "BIP_LABEL", nullable = false, length = 63)),
            @AttributeOverride(name = "description", column = @Column(name = "BIP_DESC", nullable = false)),
            @AttributeOverride(name = "optional", column = @Column(name = "BIP_OPTIONAL", nullable = false))
    })
    @AssociationOverride(
            name = "type",
            joinColumns = @JoinColumn(name = "BIP_TPE_ID", nullable = false),
            foreignKey = @ForeignKey(name = "BIP_TPE_FK")
    )
    public static class Input extends DocumentedType {
    }

}
