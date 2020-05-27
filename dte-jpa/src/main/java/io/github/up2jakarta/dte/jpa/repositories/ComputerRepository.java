package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.loader.Finder;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.views.VComputer;

import java.util.List;

/**
 * {@link DTreeComputer} and {@link VComputer} repository.
 */
public interface ComputerRepository extends Finder<Computer> {

    /**
     * Find an existing computer by unique key groupId/label.
     *
     * @param groupId the group identifier
     * @param label   the display label
     * @return the found entity, otherwise {@code null}.
     */
    DTreeComputer find(int groupId, String label);

    /**
     * Find an existing computer by identifier.
     *
     * @param id the identifier
     * @return the found computer, otherwise {@code null}.
     */
    DTreeComputer find(long id);

    /**
     * Delete an existing computer.
     *
     * @param entity the computer to delete
     */
    void remove(DTreeComputer entity);

    /**
     * Update an existing computer.
     *
     * @param entity the computer to update
     * @param <T>    the entity type
     * @return the managed entity
     */
    <T extends DTreeComputer> T merge(T entity);

    /**
     * Save a new computer.
     *
     * @param entity the computer to save
     */
    void persist(DTreeComputer entity);

    /**
     * Find computers by the specified {@code groupId} and typing {@code inputs} and {@code outputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @param outputs the typing output names
     * @return found deciders.
     */
    List<VComputer> search(int groupId, List<String> inputs, List<String> outputs);

}
