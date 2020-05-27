package io.github.up2jakarta.dte.jpa.entities.dtn;

import io.github.up2jakarta.dte.jpa.api.dtree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.dtree.IMixedComputer;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Group;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

import static javax.persistence.CascadeType.ALL;

/**
 * {@link IMixedComputer} implementation.
 */
@Entity
@DiscriminatorValue(value = "C")
public class DTreeMixedComputer extends DTreeComputer implements IMixedComputer {

    @Column(name = "DTN_DEPTH")
    private final int depth = 0;

    @OneToMany(mappedBy = "parent", targetEntity = DTreeChildNode.class, cascade = ALL, orphanRemoval = true)
    private Set<IChildNode> children = new HashSet<>();

    @OneToMany(mappedBy = "root", targetEntity = DTreeChildNode.class)
    @OrderBy("depth ASC, order ASC")
    private SortedSet<IChildNode> nodes = new TreeSet<>();

    /**
     * JPA default constructor for DTreeMixedComputer.
     */
    @Deprecated
    DTreeMixedComputer() {
        super(null);
    }

    /**
     * Public constructor for DTreeMixedComputer.
     *
     * @param group the group
     */
    public DTreeMixedComputer(final Group group) {
        super(group);
    }

    @Override
    public Integer getDepth() {
        return depth;
    }

    @Override
    public SortedSet<IChildNode> getNodes() {
        return nodes;
    }

    @Override
    public Set<IChildNode> getChildren() {
        return children;
    }

}
