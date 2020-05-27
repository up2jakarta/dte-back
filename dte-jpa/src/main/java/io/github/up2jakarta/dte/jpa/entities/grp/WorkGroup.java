package io.github.up2jakarta.dte.jpa.entities.grp;

import io.github.up2jakarta.dte.jpa.entities.Group;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

/**
 * Work Group entity.
 */
@Entity
@DiscriminatorValue(value = "G")
public class WorkGroup extends Group<Workspace> {

    /**
     * JPA default constructor for WorkGroup.
     */
    @Deprecated
    WorkGroup() {
        super(null);
    }

    /**
     * Public constructor for WorkGroup.
     *
     * @param group the parent group
     */
    public WorkGroup(final Workspace group) {
        super(group);
    }

}
