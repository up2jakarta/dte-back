package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.btree.IMixedDecider;
import io.github.up2jakarta.dte.jpa.converters.OperatorConverter;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static javax.persistence.CascadeType.ALL;

/**
 * {@link IMixedDecider} implementation.
 */
@Entity
@DiscriminatorValue(value = "C")
public class BTreeMixedDecider extends BTreeDecider implements IMixedDecider {

    @Column(name = "BTN_OPERATOR")
    @Convert(converter = OperatorConverter.class)
    private Operator operator;

    @OneToMany(mappedBy = "parent", targetEntity = BTreeChildNode.class, cascade = ALL, orphanRemoval = true)
    private Set<IChildNode> operands = new HashSet<>();

    @OneToMany(mappedBy = "root", targetEntity = BTreeChildNode.class)
    @OrderBy("depth ASC, order ASC")
    private SortedSet<IChildNode> nodes = new TreeSet<>();

    /**
     * JPA default constructor for BTreeMixedDecider.
     */
    @Deprecated
    BTreeMixedDecider() {
        super(null, 0);
    }

    /**
     * Public Constructor for BTreeMixedDecider.
     *
     * @param group the group
     */
    public BTreeMixedDecider(final Group group) {
        super(group, 0);
    }

    @Override
    public IMixedDecider getRoot() {
        return this;
    }

    @Override
    public Integer getDepth() {
        return 0;
    }

    @Override
    public SortedSet<IChildNode> getNodes() {
        return nodes;
    }

    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public void setOperator(final Operator operator) {
        this.operator = operator;
    }

    @Override
    public Set<IChildNode> getOperands() {
        return operands;
    }

}
