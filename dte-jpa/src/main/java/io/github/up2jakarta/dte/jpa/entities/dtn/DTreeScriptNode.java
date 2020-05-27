package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.IGroup;
import io.github.up2jakarta.dte.jpa.api.Script;
import io.github.up2jakarta.dte.jpa.api.Shareable;
import io.github.up2jakarta.dte.jpa.api.dtree.IParentNode;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Abstract shareable {@link Script} child node.
 */
@Entity
@DiscriminatorValue(value = "§")
public class DTreeScriptNode extends DTreeChildNode implements Script<Template>, Shareable {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "DTN_SCRIPT")
    @Type(type = "org.hibernate.type.TextType")
    private String script;

    @Column(name = "DTN_SHARED")
    private Boolean shared;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "DTN_DESC")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Group.class)
    @JoinColumn(name = "DTN_GRP_ID", foreignKey = @ForeignKey(name = "DTN_GRP_FK"))
    private IGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "DTN_TPL_ID", foreignKey = @ForeignKey(name = "DTN_TPL_FK"))
    private Template template;

    @ElementCollection
    @CollectionTable(name = "DTE_DIP", joinColumns = @JoinColumn(name = "DIP_DTN_ID"))
    @MapKeyColumn(name = "DIP_NAME", length = 31)
    private Map<String, Input> typing = new HashMap<>();

    /**
     * Protected constructor for BTreeLocalNode.
     *
     * @param group the group
     */
    @SuppressWarnings("deprecation")
    DTreeScriptNode(final Group group) {
        this.shared = false;
        this.group = group;
    }

    @Override
    public void setParent(final IParentNode parent) {
        super.setParent(parent);
        this.group = Optional.ofNullable(parent).map(p -> p.getRoot().getGroup()).orElse(null);
    }

    @Override
    public String getScript() {
        return this.script;
    }

    @Override
    public void setScript(final String script) {
        this.script = script;
    }

    @Override
    public IGroup getGroup() {
        return group;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public void setDescription(final String desc) {
        this.description = desc;
    }

    @Override
    public boolean isShared() {
        return shared;
    }

    @Override
    public void setShared(final boolean shared) {
        this.shared = shared;
    }

    @Override
    public Template getTemplate() {
        return template;
    }

    @Override
    public void setTemplate(final Template template) {
        this.template = template;
    }

    /**
     * Return the execution context input mapping name/variable.
     *
     * @return The context input.
     */
    public Map<String, Input> getTyping() {
        if (typing == null) {
            typing = new HashMap<>();
        }
        return typing;
    }

}
