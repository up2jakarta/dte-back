package io.github.up2jakarta.dte.jpa.managers;

import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.jpa.views.VDecider;

import java.util.List;

/**
 * {@link Decider} Manager.
 */
public interface DeciderManager {

    /**
     * Create a list of binary trees without details from the given source {@code entity}.
     * Set only the id, label and description properties.
     *
     * @param list the list of managed entities
     * @return the converted binary tree
     */
    List<Decider> convert(List<VDecider> list);

    /**
     * Create binary tree from the given source {@code entity}.
     *
     * @param entity the managed entity
     * @return the converted binary tree
     */
    Decider convert(BTreeDecider entity);

    /**
     * Creates a decider entity from the given {@code model}.
     *
     * @param model the binary tree
     * @return transient entity
     */
    BTreeDecider create(Decider model);

    /**
     * Updates the given {@code entity} from the given {@code model}.
     *
     * @param entity the decider entity
     * @param model  the binary tree
     * @return the given decider entity
     */
    BTreeDecider update(BTreeDecider entity, Decider model);

}
