package io.github.up2jakarta.dte.jpa.entities.tpe;

import io.github.up2jakarta.dte.jpa.entities.Type;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Basic type entity (technical).
 */
@Entity
@DiscriminatorValue(value = "B")
public class BasicType extends Type {

    @Column(name = "TPE_NAME", length = 127, nullable = false)
    private Class type;

    /**
     * JPA default constructor for BasicType.
     */
    @Deprecated
    BasicType() {
        super(null);
    }

    @Override
    public Class getJavaType() {
        return type;
    }

    /**
     * Set the java class.
     *
     * @param type the class
     */
    public void setJavaType(final Class type) {
        this.type = type;
    }
}
