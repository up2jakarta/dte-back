package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.api.IType;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;

import javax.persistence.*;

/**
 * Abstract {@link IType} (TPE).
 */
@Entity
@Table(name = "DTE_TPE", uniqueConstraints = {
        @UniqueConstraint(name = "TPE_LABEL_UC", columnNames = {"TPE_LABEL"}),
        @UniqueConstraint(name = "TPE_CLASS_UC", columnNames = {"TPE_NAME"})
})
@AttributeOverrides({
        @AttributeOverride(name = "label", column = @Column(name = "TPE_LABEL", nullable = false, length = 63)),
        @AttributeOverride(name = "description", column = @Column(name = "TPE_DESC", nullable = false))
})
@AssociationOverride(
        name = "group",
        joinColumns = @JoinColumn(name = "TPE_GRP_ID", nullable = false),
        foreignKey = @ForeignKey(name = "TPE_GRP_FK")
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TPE_TYPE", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "?")
public abstract class Type extends Document implements IType {

    private static final String SEQUENCE = "DTE_TPE_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, initialValue = 99, allocationSize = 1)
    @Column(name = "TPE_ID", nullable = false)
    private Integer id;

    /**
     * JPA default constructor for Type.
     */
    protected Type() {
        super(null);
    }

    /**
     * Protected constructor for Type.
     *
     * @param group the type group
     */
    protected Type(final AbstractGroup group) {
        super(group);
    }

    @Override
    public Integer getId() {
        return id;
    }

}
