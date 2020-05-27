package io.github.up2jakarta.dte.jpa.entities.tpe;

import io.github.up2jakarta.dte.jpa.entities.Group;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Generated type entity, useful declared output.
 */
@Entity
@DiscriminatorValue(value = "G")
public class GeneratedType extends DefinedType {

    @Column(name = "TPE_NAME", length = 127, nullable = false)
    private Class type;

    /**
     * JPA default constructor for GeneratedType.
     */
    @Deprecated
    GeneratedType() {
        super(null);
    }

    /**
     * Public constructor for BasicTemplate.
     *
     * @param group the type group
     */
    public GeneratedType(final Group group) {
        super(group);
    }

    @Override
    public Class getJavaType() {
        return type;
    }

    /**
     * Set the static java class.
     *
     * @param type the class
     */
    public void setJavaType(final Class type) {
        this.type = type;
    }
}
