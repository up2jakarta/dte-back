package io.github.up2jakarta.dte.jpa.entities.grp;

import io.github.up2jakarta.dte.jpa.entities.Group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Group Workspace entity.
 */
@Entity
@DiscriminatorValue(value = "S")
public class Workspace extends Group<MainGroup> {

    @OneToMany(mappedBy = "parent")
    private Set<WorkGroup> groups = new HashSet<>();

    /**
     * JPA default constructor for Workspace.
     */
    @Deprecated
    Workspace() {
        super(null);
    }

    /**
     * Public constructor for WorkSpace.
     *
     * @param group the main group
     */
    public Workspace(final MainGroup group) {
        super(group);
    }

    /**
     * @return the work groups.
     */
    public Set<WorkGroup> getGroups() {
        return groups;
    }
}
