package io.github.up2jakarta.dte.web.managers;

import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup;
import io.github.up2jakarta.dte.web.models.GroupModel;

import java.util.List;

/**
 * {@link Group} Manager.
 */
public interface GroupManager {

    /**
     * Create a list of groups without details from the given source {@code list}.
     * Set only the following properties: id, label, icon and color, parentId.
     *
     * @param list the list of managed entities
     * @return the converted groups
     */
    List<GroupModel> build(List<? extends Group> list);

    /**
     * Create a list of groups without details from the given source {@code list}.
     * Set only the following properties: id, label, icon and color.
     *
     * @param list the list of managed entities
     * @return the converted group
     */
    List<GroupModel> convert(List<? extends Group> list);

    /**
     * Create group from the given source {@code entity}.
     *
     * @param entity the managed entity
     * @return the converted group
     */
    GroupModel convert(Group entity);

    /**
     * Creates a group entity from the given {@code model}.
     *
     * @param model the group
     * @return transient entity
     */
    Group create(GroupModel model);

    /**
     * Updates the given {@code entity} from the given {@code model}.
     *
     * @param entity the group entity
     * @param model  the group
     * @return the given group entity
     */
    Group update(Group entity, GroupModel model);

    /**
     * Update the main properties of the given {@code model} from the source {@code entity}.
     *
     * @param entity the entity
     * @param model  the group model
     * @return the updated model
     */
    GroupModel fill(AbstractGroup entity, GroupModel model);

}
