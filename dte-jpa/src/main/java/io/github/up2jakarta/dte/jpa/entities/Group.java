package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;

import javax.persistence.*;
import java.util.Objects;

/**
 * Abstract Group entity.
 *
 * @param <P> the parent type
 */
@Entity
@DiscriminatorValue(value = "!")
public abstract class Group<P extends AbstractGroup> extends AbstractGroup {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = AbstractGroup.class, optional = false)
    @JoinColumn(name = "GRP_PARENT_ID", foreignKey = @ForeignKey(name = "GRP_PARENT_FK"))
    private P parent;

    /**
     * JPA default constructor for Group.
     */
    protected Group() {
    }

    /**
     * Protected constructor for Group.
     *
     * @param parent the parent group
     */
    protected Group(final P parent) {
        this.parent = parent;
    }

    /**
     * @return the parent group.
     */
    public P getParent() {
        return parent;
    }

    @Override
    public final boolean equals(final Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof Group)) {
            return false;
        }
        final Group that = (Group) other;
        return Objects.equals(getLabel(), that.getLabel()) && Objects.equals(getParent(), that.getParent());
    }

    @Override
    public final int hashCode() {
        return Objects.hashCode(getLabel());
    }

    @Override
    public final String toString() {
        return String.valueOf(getParent()) + '/' + getLabel();
    }
}
