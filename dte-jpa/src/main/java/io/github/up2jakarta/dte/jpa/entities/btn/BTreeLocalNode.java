package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.jpa.api.IDecider;
import io.github.up2jakarta.dte.jpa.api.IGroup;
import io.github.up2jakarta.dte.jpa.api.btree.ILeafNode;
import io.github.up2jakarta.dte.jpa.api.btree.IOperatorNode;
import io.github.up2jakarta.dte.jpa.api.btree.IPlainDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.Template;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * {@link IPlainDecider} leaf node implementation.
 */
@Entity
@DiscriminatorValue(value = "T")
public class BTreeLocalNode extends BTreeChildNode implements ILeafNode, IPlainDecider<Template> {

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Type(type = "org.hibernate.type.TextType")
    @Column(name = "BTN_DESC")
    private String description;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "BTN_SCRIPT")
    @Type(type = "org.hibernate.type.TextType")
    private String script;

    @Column(name = "BTN_SHARED")
    private Boolean shared;

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = Group.class)
    @JoinColumn(name = "BTN_GRP_ID", foreignKey = @ForeignKey(name = "BTN_GRP_FK"))
    private IGroup group;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "BTN_TPL_ID", foreignKey = @ForeignKey(name = "BTN_TPL_FK"))
    private Template template;

    @ElementCollection
    @CollectionTable(name = "DTE_BIP", joinColumns = @JoinColumn(name = "BIP_BTN_ID"))
    @MapKeyColumn(name = "BIP_NAME", length = 31)
    private final Map<String, Input> typing = new HashMap<>();

    /**
     * JPA default constructor for BTreeLocalNode.
     */
    @Deprecated
    BTreeLocalNode() {
        this(null);
    }

    /**
     * Protected constructor for BTreeLocalNode.
     *
     * @param group the group
     */
    public BTreeLocalNode(final Group group) {
        this.shared = false;
        this.group = group;
    }

    @Override
    public void setParent(final IOperatorNode parent) {
        super.parent = parent;
        super.root = Optional.ofNullable(parent).map(IOperatorNode::getRoot).orElse(null);
        super.depth = Optional.ofNullable(parent).map(IOperatorNode::getDepth).map(d -> d + 1).orElse(null);
        this.group = Optional.ofNullable(parent).map(p -> p.getRoot().getGroup()).orElse(null);
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
    public IGroup getGroup() {
        return group;
    }

    @Override
    public String getDescription() {
        return description;
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

    @Override
    public Map<String, Input> getTyping() {
        return typing;
    }

    @Override
    public IDecider getDecider() {
        return this;
    }
}
