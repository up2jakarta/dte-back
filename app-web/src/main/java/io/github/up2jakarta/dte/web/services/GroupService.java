package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.web.models.GroupModel;

import java.util.List;

/**
 * Service responsible for groups management.
 */
public interface GroupService {

    /**
     * Find an existing group by identifier.
     *
     * @param id the identifier
     * @return the found group
     */
    GroupModel find(int id);

    /**
     * Delete an existing group by identifier.
     *
     * @param id the identifier
     */
    void delete(int id);

    /**
     * Update an existing group.
     *
     * @param id    the identifier
     * @param model the model with updated properties
     * @return the group after update.
     */
    GroupModel update(int id, GroupModel model);

    /**
     * Create a new group.
     *
     * @param model the new group
     * @return the new created group.
     */
    GroupModel create(GroupModel model);

    /**
     * Search groups by parent's group identifier.
     *
     * @param parentId the parent identifier
     * @return the found groups
     */
    List<GroupModel> search(Integer parentId);

    /**
     * Retrieve all groups hierarchy within workspaces are at the first level.
     *
     * @return the groups tree
     */
    List<GroupModel> tree();

    /**
     * Retrieve all groups map identifier/label (list of values).
     *
     * @return the found groups
     */
    List<GroupModel> lov();

}
