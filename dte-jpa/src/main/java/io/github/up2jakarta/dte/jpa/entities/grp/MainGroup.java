package io.github.up2jakarta.dte.jpa.entities.grp;

import io.github.up2jakarta.dte.jpa.repositories.GroupRepository;
import org.hibernate.annotations.Where;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.HashSet;
import java.util.Set;

/**
 * Main Group entity (technical).
 */
@Entity
@DiscriminatorValue(value = "M")
public class MainGroup extends AbstractGroup {

    @OneToMany(mappedBy = "parent", targetEntity = Workspace.class)
    @Where(clause = "GRP_TYPE = 'S'")
    private Set<Workspace> spaces = new HashSet<>();

    /**
     * JPA default constructor for MainGroup.
     *
     * @see GroupRepository#getMain()
     */
    @Deprecated
    public MainGroup() {
    }

    /**
     * @return the workspaces
     */
    public Set<Workspace> getSpaces() {
        return spaces;
    }

    @Override
    public final boolean equals(final Object other) {
        // Single instance
        return other instanceof MainGroup;
    }

    @Override
    public final int hashCode() {
        return 0;
    }

    @Override
    public final String toString() {
        return getLabel();
    }

}
