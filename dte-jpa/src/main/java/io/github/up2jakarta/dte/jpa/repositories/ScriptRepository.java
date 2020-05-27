package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.jpa.views.Key;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VDScript;

import java.util.List;

/**
 * {@link VDScript} and {@link VCScript} Repository.
 */
public interface ScriptRepository {

    /**
     * Find an existing script based decider by identifier.
     *
     * @param id the unique identifier
     * @return the found entity, otherwise {@code null}.
     */
    VDScript find(Key id);

    /**
     * Find script based deciders by the specified {@code groupId} and typing {@code inputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @return found scripts.
     */
    List<VDScript> search(int groupId, List<String> inputs);

    /**
     * Find an existing script based computer by identifier.
     *
     * @param id the unique identifier
     * @return the found entity, otherwise {@code null}.
     */
    VCScript find(long id);

    /**
     * Find script based computers by the specified {@code groupId} and typing {@code inputs} and {@code outputs}.
     *
     * @param groupId the group identifier
     * @param inputs  the typing input names
     * @param outputs the typing output names
     * @return found scripts.
     */
    List<VCScript> search(int groupId, List<String> inputs, List<String> outputs);

}
