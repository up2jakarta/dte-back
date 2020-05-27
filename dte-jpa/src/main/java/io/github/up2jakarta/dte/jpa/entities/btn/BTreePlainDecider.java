package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.jpa.api.btree.IPlainDecider;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import org.hibernate.annotations.Type;

import javax.persistence.*;

/**
 * {@link IPlainDecider} implementation.
 */
@Entity
@DiscriminatorValue(value = "S")
public class BTreePlainDecider extends BTreeDecider implements IPlainDecider<Template> {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BTN_SCRIPT")
    @Type(type = "org.hibernate.type.TextType")
    private String script;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BTN_TPL_ID", foreignKey = @ForeignKey(name = "BTN_TPL_FK"))
    private Template template;

    /**
     * JPA default constructor for BTreePlainDecider.
     */
    @Deprecated
    BTreePlainDecider() {
        this(null);
    }

    /**
     * Public constructor for BTreePlainDecider.
     *
     * @param group the group
     */
    public BTreePlainDecider(final Group group) {
        super(group, null);
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
