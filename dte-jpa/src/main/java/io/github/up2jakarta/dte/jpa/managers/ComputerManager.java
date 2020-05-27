package io.github.up2jakarta.dte.jpa.managers;

import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.views.VComputer;

import java.util.List;

/**
 * {@link Computer} Manager.
 */
public interface ComputerManager {

    /**
     * Create a list of decision trees without details from the given source {@code entity}.
     * Set only the id, label and description properties.
     *
     * @param list the list of managed entities
     * @return the converted decision tree
     */
    List<Computer> convert(List<VComputer> list);

    /**
     * Create decision tree from the given source {@code entity}.
     *
     * @param entity the managed entity
     * @return the converted decision tree
     */
    Computer convert(DTreeComputer entity);

    /**
     * Creates a computer entity from the given {@code model}.
     *
     * @param model the decision tree
     * @return transient entity
     */
    DTreeComputer create(Computer model);

    /**
     * Updates the given {@code entity} from the given {@code model}.
     *
     * @param entity the computer entity
     * @param model  the decision tree
     * @return the given computer entity
     */
    DTreeComputer update(DTreeComputer entity, Computer model);

}
