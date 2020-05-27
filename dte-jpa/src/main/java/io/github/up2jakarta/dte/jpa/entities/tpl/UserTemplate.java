package io.github.up2jakarta.dte.jpa.entities.tpl;

import groovy.lang.Script;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;

import javax.persistence.*;

/**
 * User template entity based on script source code.
 */
@Entity
@DiscriminatorValue(value = "U")
public class UserTemplate extends Template {

    @Column(name = "TPL_NAME", length = 127, nullable = false)
    private String name;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "TPL_SCRIPT")
    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TPL_BASE_ID", foreignKey = @ForeignKey(name = "TPL_BASE_FK"))
    private Template base;

    @Transient
    private Class<? extends Script> generated;

    /**
     * JPA default constructor for UserTemplate.
     */
    @Deprecated
    UserTemplate() {
        super(null);
    }

    /**
     * Public constructor for UserTemplate.
     *
     * @param group the template group
     */
    public UserTemplate(final Group group) {
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

    /**
     * @return the source code of the script base class.
     */
    public String getScript() {
        return script;
    }

    /**
     * Return the source code of the script base class.
     *
     * @param script the source code
     */
    public void setScript(final String script) {
        this.script = script;
    }

    /**
     * @return the base template
     */
    public Template getBase() {
        return base;
    }

    /**
     * Set the base template, i.e the template which inherit from.
     *
     * @param base the base template
     */
    public void setBase(final Template base) {
        this.base = base;
    }

    @Override
    public Class<? extends Script> getBaseClass() {
        if (generated == null) {
            generated = Script.class;
        }
        return generated;
    }
}
