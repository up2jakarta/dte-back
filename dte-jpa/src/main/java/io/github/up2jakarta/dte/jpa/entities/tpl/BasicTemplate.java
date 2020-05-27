package io.github.up2jakarta.dte.jpa.entities.tpl;

import groovy.lang.Script;
import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;

import javax.persistence.Column;
import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Basic template entity based on java class.
 */
@Entity
@DiscriminatorValue(value = "B")
public class BasicTemplate extends Template {

    @Column(name = "TPL_NAME", length = 127, nullable = false)
    private Class<? extends Script> base;

    /**
     * JPA default constructor for BasicTemplate.
     */
    @Deprecated
    BasicTemplate() {
        super(null);
    }

    /**
     * Public constructor for BasicTemplate.
     *
     * @param group the template group
     */
    public BasicTemplate(final AbstractGroup group) {
        super(group);
    }

    @Override
    public Class<? extends Script> getBaseClass() {
        return base;
    }

    /**
     * Set the template java base class.
     *
     * @param base the base class
     */
    public void setBaseClass(final Class<? extends Script> base) {
        this.base = base;
    }
}
