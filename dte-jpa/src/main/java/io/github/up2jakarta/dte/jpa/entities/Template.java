package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.api.ITemplate;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;

import javax.persistence.*;

/**
 * Abstract {@link ITemplate} (TPL).
 */
@Entity
@Table(name = "DTE_TPL", uniqueConstraints = {
        @UniqueConstraint(name = "TPL_LABEL_UC", columnNames = {"TPL_GRP_ID", "TPL_LABEL"}),
        @UniqueConstraint(name = "TPL_NAME_UC", columnNames = {"TPL_GRP_ID", "TPL_NAME"})
})
@AttributeOverrides({
        @AttributeOverride(name = "label", column = @Column(name = "TPL_LABEL", nullable = false, length = 63)),
        @AttributeOverride(name = "description", column = @Column(name = "TPL_DESC", nullable = false))
})
@AssociationOverride(
        name = "group",
        joinColumns = @JoinColumn(name = "TPL_GRP_ID", nullable = false),
        foreignKey = @ForeignKey(name = "TPL_GRP_FK")
)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "TPL_TYPE", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "?")
public abstract class Template extends Document implements ITemplate {

    private static final String SEQUENCE = "DTE_TPL_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, initialValue = 99, allocationSize = 1)
    @Column(name = "TPL_ID", nullable = false)
    private Integer id;

    /**
     * JPA default constructor for Template.
     */
    protected Template() {
        super(null);
    }

    /**
     * Protected constructor for Template.
     *
     * @param group the template group
     */
    protected Template(final AbstractGroup group) {
        super(group);
    }

    @Override
    public Integer getId() {
        return id;
    }

}
