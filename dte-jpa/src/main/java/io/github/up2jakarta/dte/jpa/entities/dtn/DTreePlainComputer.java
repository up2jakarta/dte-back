package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.dtree.IPlainComputer;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * {@link IPlainComputer} implementation.
 */
@Entity
@DiscriminatorValue(value = "S")
public class DTreePlainComputer extends DTreeComputer implements IPlainComputer<Template> {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DTN_SCRIPT")
    @Type(type = "org.hibernate.type.TextType")
    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DTN_TPL_ID", foreignKey = @ForeignKey(name = "DTN_TPL_FK"))
    private Template template;

    /**
     * JPA default constructor for DTreePlainComputer.
     */
    @Deprecated
    DTreePlainComputer() {
        super(null);
    }

    /**
     * Public constructor for DTreePlainComputer.
     *
     * @param group the group
     */
    public DTreePlainComputer(final Group group) {
        super(group);
    }

    @Override
    public String getScript() {
        return script;
    }

    @Override
    public void setScript(final String script) {
        this.script = script;
    }

    @Override
    public Template getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(final Template template) {
        this.template = template;
    }
}
