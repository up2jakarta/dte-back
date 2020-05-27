package io.github.up2jakarta.dte.jpa.entities.grp;

import io.github.up2jakarta.dte.jpa.api.IGroup;
import io.github.up2jakarta.dte.jpa.api.IParameter;
import io.github.up2jakarta.dte.jpa.entities.DocumentedType;
import io.github.up2jakarta.dte.jpa.entities.ShareableType;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.entities.tpe.BasicType;

import javax.persistence.*;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * Abstract Base {@link IGroup} (GRP).
 */
@Entity
@Table(name = "DTE_GRP", uniqueConstraints = {
        @UniqueConstraint(name = "DTE_GRP_LABEL_UC", columnNames = {"GRP_LABEL", "GRP_PARENT_ID"})
})
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "GRP_TYPE", discriminatorType = DiscriminatorType.CHAR)
@DiscriminatorValue(value = "?")
public abstract class AbstractGroup implements IGroup {

    private static final String SEQUENCE = "DTE_GRP_SEQ";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = SEQUENCE)
    @SequenceGenerator(name = SEQUENCE, sequenceName = SEQUENCE, initialValue = 99, allocationSize = 1)
    @Column(name = "GRP_ID", nullable = false)
    private Integer id;

    @Column(name = "GRP_LABEL", length = 63, nullable = false)
    private String label;

    @Column(name = "GRP_ICON", length = 31)
    private String icon;

    @Column(name = "GRP_COLOR", length = 15)
    private String color;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    @Column(name = "GRP_DESC", nullable = false)
    private String description;

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Type> types = new HashSet<>();

    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Template> templates = new HashSet<>();

    @ElementCollection
    @CollectionTable(name = "DTE_PMR", joinColumns = {
            @JoinColumn(name = "PMR_GRP_ID", foreignKey = @ForeignKey(name = "PMR_GRP_FK"))
    })
    @MapKeyColumn(name = "PMR_NAME", length = 127)
    private final Map<String, Parameter> parameters = new HashMap<>();

    @ElementCollection
    @CollectionTable(
            name = "DTE_GIP",
            joinColumns = @JoinColumn(name = "GIP_GRP_ID", foreignKey = @ForeignKey(name = "GIP_GRP_FK")),
            uniqueConstraints = @UniqueConstraint(name = "GIP_LABEL_UC", columnNames = {"GIP_GRP_ID", "GIP_LABEL"})
    )
    @MapKeyColumn(name = "GIP_NAME", length = 31)
    private final Map<String, Input> typing = new HashMap<>();

    @ElementCollection
    @CollectionTable(
            name = "DTE_GOP",
            joinColumns = @JoinColumn(name = "GOP_GRP_ID", foreignKey = @ForeignKey(name = "GOP_GRP_FK")),
            uniqueConstraints = @UniqueConstraint(name = "GOP_LABEL_UC", columnNames = {"GOP_GRP_ID", "GOP_LABEL"})
    )
    @MapKeyColumn(name = "GOP_NAME", length = 31)
    private final Map<String, Output> declaredTyping = new HashMap<>();

    /**
     * JPA default constructor for AbstractGroup.
     */
    protected AbstractGroup() {
    }

    @Override
    public Integer getId() {
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
    public String getIcon() {
        return icon;
    }

    @Override
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    @Override
    public String getColor() {
        return color;
    }

    @Override
    public void setColor(final String color) {
        this.color = color;
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
    public Set<Template> getTemplates() {
        return templates;
    }

    @Override
    public Set<Type> getTypes() {
        return types;
    }

    @Override
    public Map<String, Parameter> getParameters() {
        return parameters;
    }

    @Override
    public Map<String, Input> getTyping() {
        return typing;
    }

    @Override
    public Map<String, Output> getDeclaredTyping() {
        return declaredTyping;
    }


    /**
     * Group typing input.
     */
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "label", column = @Column(name = "GIP_LABEL", nullable = false, length = 63)),
            @AttributeOverride(name = "description", column = @Column(name = "GIP_DESC", nullable = false)),
            @AttributeOverride(name = "optional", column = @Column(name = "GIP_OPTIONAL", nullable = false))
    })
    @AssociationOverride(
            name = "type",
            joinColumns = @JoinColumn(name = "GIP_TPE_ID", nullable = false),
            foreignKey = @ForeignKey(name = "GIP_TPE_FK")
    )
    public class Input extends DocumentedType {
    }

    /**
     * Group typing output.
     */
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "label", column = @Column(name = "GOP_LABEL", nullable = false, length = 63)),
            @AttributeOverride(name = "description", column = @Column(name = "GOP_DESC", nullable = false)),
            @AttributeOverride(name = "optional", column = @Column(name = "GOP_OPTIONAL", nullable = false)),
            @AttributeOverride(name = "shared", column = @Column(name = "GOP_SHARED", nullable = false))
    })
    @AssociationOverride(
            name = "type",
            joinColumns = @JoinColumn(name = "GOP_TPE_ID", nullable = false),
            foreignKey = @ForeignKey(name = "GOP_TPE_FK")
    )
    public class Output extends ShareableType {
    }

    /**
     * Group parameter.
     */
    @Embeddable
    public class Parameter implements IParameter<BasicType> {

        @Column(name = "PMR_LABEL", length = 63, nullable = false)
        private String label;

        @Lob
        @Basic(fetch = FetchType.LAZY)
        @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
        @Column(name = "PMR_DESC", nullable = false)
        private String description;

        @Column(name = "PMR_VALUE", length = 511, nullable = false)
        private String value;

        @ManyToOne(fetch = FetchType.LAZY, optional = false)
        @JoinColumn(name = "PMR_TPE_ID", foreignKey = @ForeignKey(name = "PMR_TPE_FK"))
        private BasicType type;


        @Override
        public String getLabel() {
            return label;
        }

        @Override
        public void setLabel(final String label) {
            this.label = label;
        }

        @Override
        public String getDescription() {
            return description;
        }

        @Override
        public void setDescription(final String description) {
            this.description = description;
        }

        @Override
        public String getValue() {
            return value;
        }

        @Override
        public void setValue(final String value) {
            this.value = value;
        }

        @Override
        public BasicType getType() {
            return type;
        }

        @Override
        public void setType(final BasicType type) {
            this.type = type;
        }

        @Override
        public String toString() {
            return String.valueOf(AbstractGroup.this) + '/' + getLabel();
        }
    }
}
