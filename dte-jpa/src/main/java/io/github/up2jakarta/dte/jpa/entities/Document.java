package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.jpa.api.Documented;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;

import javax.persistence.*;
import java.util.Objects;

/**
 * Abstract identifiable {@link Documented}.
 */
@MappedSuperclass
public abstract class Document implements Identifiable<Integer>, Documented {

    private String label;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @org.hibernate.annotations.Type(type = "org.hibernate.type.TextType")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private final AbstractGroup group;

    /***
     * Protected constructor for Document.
     *
     * @param group the document group
     */
    Document(final AbstractGroup group) {
        this.group = group;
    }

    /**
     * @return the related group.
     */
    public AbstractGroup getGroup() {
        return group;
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
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Template)) {
            return false;
        }
        final Template that = (Template) other;
        return Objects.equals(getLabel(), that.getLabel()) && Objects.equals(getGroup(), that.getGroup());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getLabel());
    }

    @Override
    public final String toString() {
        return String.valueOf(getGroup()) + '/' + getLabel();
    }
}
