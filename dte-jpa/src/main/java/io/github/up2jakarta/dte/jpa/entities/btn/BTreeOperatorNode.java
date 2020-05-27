package io.github.up2jakarta.dte.jpa.entities.btn;

import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.btree.IOperatorNode;
import io.github.up2jakarta.dte.jpa.converters.OperatorConverter;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

import static javax.persistence.CascadeType.ALL;

/**
 * {@link IOperatorNode} implementation.
 */
@Entity
@DiscriminatorValue(value = "P")
public class BTreeOperatorNode extends BTreeChildNode implements IOperatorNode {

    @Column(name = "BTN_OPERATOR")
    @Convert(converter = OperatorConverter.class)
    private Operator operator;

    @OneToMany(mappedBy = "parent", targetEntity = BTreeChildNode.class, cascade = ALL, orphanRemoval = true)
    private Set<IChildNode> operands = new HashSet<>();


    @Override
    public Operator getOperator() {
        return operator;
    }

    @Override
    public void setOperator(final Operator operator) {
        this.operator = operator;
        setLabel(operator.name());
    }

    @Override
    public Set<IChildNode> getOperands() {
        return operands;
    }
}
