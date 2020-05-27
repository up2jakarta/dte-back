package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.IComputer;
import io.github.up2jakarta.dte.jpa.api.dtree.IComputerNode;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;

import javax.persistence.*;

/**
 * {@link IComputerNode} computer node implementation.
 */
@Entity
@DiscriminatorValue(value = "R")
@SuppressWarnings("deprecation")
public class DTreeComputerNode extends DTreeChildNode implements IComputerNode {

    @ManyToOne(fetch = FetchType.LAZY, targetEntity = DTreeComputer.class)
    @JoinColumn(name = "DTN_DTN_ID", foreignKey = @ForeignKey(name = "DTN_DTN_FK"))
    private IComputer computer;


    @Override
    public IComputer getComputer() {
        return computer;
    }

    @Override
    public void setComputer(final IComputer computer) {
        this.computer = computer;
        setLabel(computer.getLabel());
    }

}
