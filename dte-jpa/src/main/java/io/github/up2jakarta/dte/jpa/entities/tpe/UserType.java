package io.github.up2jakarta.dte.jpa.entities.tpe;

import io.github.up2jakarta.dte.jpa.entities.Group;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.Transient;

/**
 * User defined type entity.
 */
@Entity
@DiscriminatorValue(value = "U")
public class UserType extends DefinedType {

    @Column(name = "TPE_NAME", length = 127, nullable = false)
    private String name;

    @Transient
    private Class generated;

    /**
     * JPA default constructor for ExtendedTemplate.
     */
    @Deprecated
    UserType() {
    }

    /**
     * Public constructor for ExtendedTemplate.
     *
     * @param group the template group
     */
    public UserType(final Group group) {
        super(group);
    }

    /**
     * Return the generated base class name.
     *
     * @return the unique name
     */
    public String getName() {
        return name;
    }

    /**
     * Set the generated base class name.
     *
     * @param name unique name
     */
    public void setName(final String name) {
        this.name = name;
    }

    @Override
    public Class getJavaType() {
        if (generated == null) {
            generated = Object.class;
        }
        return generated;
    }

}
