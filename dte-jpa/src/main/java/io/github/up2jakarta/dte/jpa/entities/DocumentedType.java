package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.api.Input;

import javax.persistence.*;

/**
 * Abstract {@link Input}.
 */
@MappedSuperclass
public abstract class DocumentedType implements Input<Type> {

    private String label;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String description;

    private Boolean optional;

    @ManyToOne(fetch = FetchType.LAZY)
    private Type type;

    /**
     * JPA default constructor for DocumentedType.
     */
    protected DocumentedType() {
        optional = true;
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
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    @Override
    public Type getType() {
        return type;
    }

    @Override
    public void setType(final Type type) {
        this.type = type;
    }

    @Override
    public boolean isOptional() {
        return optional;
    }

    @Override
    public void setOptional(final boolean optional) {
        this.optional = optional;
    }

    @Override
    public final String toString() {
        return getLabel();
    }
}
