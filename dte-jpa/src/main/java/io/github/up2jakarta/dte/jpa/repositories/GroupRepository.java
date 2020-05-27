package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.grp.MainGroup;

import java.util.List;

/**
 * {@link Group} repository.
 */
public interface GroupRepository {

    /**
     * Find an existing group by business key.
     *
     * @param parentId the parent identifier
     * @param label    the display label
     * @return the found entity, otherwise {@code null}.
     */
    Group find(int parentId, String label);

    /**
     * Find an existing group by identifier.
     *
     * @param id the identifier
     * @return the found entity, otherwise {@code null}.
     */
    Group find(int id);

    /**
     * Delete an existing group.
     *
     * @param entity the group to delete
     */
    void remove(Group entity);

    /**
     * Update an existing group.
     *
     * @param entity the group to update
     * @return the managed entity
     */
    Group merge(Group entity);

    /**
     * Save a new group.
     *
     * @param entity the group to save
     */
    void persist(Group entity);

    /**
     * Search groups by parent's group identifier ordered by label.
     *
     * @param parentId the parent identifier
     * @return the found groups
     */
    List<? extends Group> search(Integer parentId);

    /**
     * @return the technical group.
     */
    MainGroup getMain();

    /**
     * Retrieve all groups ordered by parent and label.
     *
     * @return the found groups
     */
    List<? extends Group> tree();
}
