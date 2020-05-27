package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.loader.Finder;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.views.VDecider;

import java.util.List;

/**
 * {@link BTreeDecider} and {@link VDecider} repository.
 */
public interface DeciderRepository extends Finder<Decider> {

    /**
     * Find an existing decider by unique key groupId/label.
     *
     * @param groupId the group identifier
     * @param label   the display label
     * @return the found entity, otherwise {@code null}.
     */
    BTreeDecider find(int groupId, String label);

    /**
     * Find an existing decider by identifier.
     *
     * @param id the identifier
     * @return the found entity, otherwise {@code null}.
     */
    @Override
    BTreeDecider find(long id);

    /**
     * Delete an existing decider.
     *
     * @param entity the decider to delete
     */
    void remove(BTreeDecider entity);

    /**
     * Update an existing decider.
     *
     * @param entity the decider to update
     * @param <T>    the entity type
     * @return the managed entity
     */
    <T extends BTreeDecider> T merge(T entity);

    /**
     * Save a new decider.
     *
     * @param entity the decider to save
     */
    void persist(BTreeDecider entity);

    /**
     * Find deciders by the specified {@code groupId} and typing {@code inputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @return found deciders.
     */
    List<VDecider> search(int groupId, List<String> inputs);

}
