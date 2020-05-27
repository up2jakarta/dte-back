package io.github.up2jakarta.dte.jpa.entities;

import io.github.up2jakarta.dte.jpa.api.Output;

import javax.persistence.MappedSuperclass;

/**
 * Abstract {@link Output}.
 */
@MappedSuperclass
public abstract class ShareableType extends DocumentedType implements Output<Type> {

    private Boolean shared;

    /**
     * JPA default constructor for ShareableType.
     */
    protected ShareableType() {
        shared = false;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    @Override
    public void setShared(final boolean shared) {
        this.shared = shared;
    }
}
