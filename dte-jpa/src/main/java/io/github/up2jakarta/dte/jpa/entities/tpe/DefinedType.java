package io.github.up2jakarta.dte.jpa.entities.tpe;

import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.ShareableType;
import io.github.up2jakarta.dte.jpa.entities.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

/**
 * Abstract defined type entity.
 */
@Entity
@DiscriminatorValue(value = "!")
public abstract class DefinedType extends Type {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TPE_BASE_ID", foreignKey = @ForeignKey(name = "TPE_BASE_FK"))
    private DefinedType base;

    @ElementCollection
    @CollectionTable(
            name = "DTE_FLD",
            joinColumns = @JoinColumn(name = "FLD_TYPE_ID", foreignKey = @ForeignKey(name = "FLD_TYPE_FK")),
            uniqueConstraints = @UniqueConstraint(name = "FLD_LABEL_UC", columnNames = {"FLD_TYPE_ID", "FLD_LABEL"})
    )
    @MapKeyColumn(name = "FLD_NAME", length = 31)
    private final Map<String, Field> fields = new HashMap<>();

    /**
     * JPA default constructor for DefinedType.
     */
    protected DefinedType() {
        super(null);
    }

    /**
     * Public constructor for DefinedType.
     *
     * @param group the type group
     */
    public DefinedType(final Group group) {
        super(group);
    }

    /**
     * Return the super type which inherit from.
     *
     * @return the base type
     */
    public DefinedType getBase() {
        return base;
    }

    /**
     * Set the super type which inherit from.
     *
     * @param base the base type
     */
    public void setBase(final DefinedType base) {
        this.base = base;
    }

    /**
     * Return the declared fields.
     *
     * @return the declared fields.
     */
    public Map<String, Field> getFields() {
        return fields;
    }


    /**
     * Defined type field.
     */
    @Embeddable
    @AttributeOverrides({
            @AttributeOverride(name = "label", column = @Column(name = "FLD_LABEL", nullable = false, length = 63)),
            @AttributeOverride(name = "description", column = @Column(name = "FLD_DESC", nullable = false)),
            @AttributeOverride(name = "optional", column = @Column(name = "FLD_OPTIONAL", nullable = false)),
            @AttributeOverride(name = "shared", column = @Column(name = "FLD_SHARED", nullable = false))
    })
    @AssociationOverride(
            name = "type",
            joinColumns = @JoinColumn(name = "FLD_TPE_ID", nullable = false),
            foreignKey = @ForeignKey(name = "FLD_TPE_FK")
    )
    public class Field extends ShareableType {

        @Column(name = "FLD_ARRAY", nullable = false)
        private Boolean array;

        /**
         * Default constructor for Field.
         */
        public Field() {
            array = false;
        }

        /**
         * Determines if this {@code Field} object represents an array type.
         *
         * @return {@code true} if this object represents an array type, otherwise {@code false}.
         */
        public boolean istArray() {
            return array;
        }

        /**
         * Tells the type generator if this {@code Field} represents an array type.
         *
         * @param array the array flag
         */
        public void setArray(final boolean array) {
            this.array = array;
        }
    }
}

