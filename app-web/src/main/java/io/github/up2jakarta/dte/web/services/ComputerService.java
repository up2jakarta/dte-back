package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.jpa.models.Computer;

import java.util.List;

/**
 * Service responsible for computers management.
 */
public interface ComputerService {

    /**
     * Find an existing computer by identifier.
     *
     * @param id the identifier
     * @return the found computer
     */
    Computer find(Long id);

    /**
     * Delete an existing computer by identifier.
     *
     * @param id the identifier
     */
    void delete(Long id);

    /**
     * Update an existing computer.
     *
     * @param id    the identifier
     * @param model the model with updated properties
     * @return the computer after update.
     */
    Computer update(Long id, Computer model);

    /**
     * Create a new computer.
     *
     * @param model the new computer
     * @return the new created computer.
     */
    Computer create(Computer model);

    /**
     * Find an existing computer identifier by unique key groupId/label.
     *
     * @param groupId the group identifier
     * @param label   the display label
     * @return the found entity identifier, otherwise {@code null}.
     */
    Long find(int groupId, String label);

    /**
     * Find computers by the specified {@code groupId} and typing {@code inputs} and {@code outputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @param outputs the typing output names
     * @return found deciders.
     */
    List<Computer> search(int groupId, List<String> inputs, List<String> outputs);

}
