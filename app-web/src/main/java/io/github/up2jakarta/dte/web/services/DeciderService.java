package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.jpa.models.Decider;

import java.util.List;

/**
 * Service responsible for deciders management.
 */
public interface DeciderService {

    /**
     * Find an existing decider by identifier.
     *
     * @param id the identifier
     * @return the found decider
     */
    Decider find(Long id);

    /**
     * Delete an existing decider by identifier.
     *
     * @param id the identifier
     */
    void delete(Long id);

    /**
     * Update an existing decider.
     *
     * @param id    the identifier
     * @param model the model with updated properties
     * @return the decider after update.
     */
    Decider update(Long id, Decider model);

    /**
     * Create a new decider.
     *
     * @param model the new decider
     * @return the new created decider.
     */
    Decider create(Decider model);

    /**
     * Find an existing decider identifier by unique key groupId/label.
     *
     * @param groupId the group identifier
     * @param label   the display label
     * @return the found entity identifier, otherwise {@code null}.
     */
    Long find(int groupId, String label);

    /**
     * Find deciders by the specified {@code groupId} and typing {@code inputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @return the found deciders
     */
    List<Decider> search(int groupId, List<String> inputs);

}
