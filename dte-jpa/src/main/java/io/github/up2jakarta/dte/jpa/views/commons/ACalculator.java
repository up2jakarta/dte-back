package io.github.up2jakarta.dte.jpa.views.commons;

import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.util.Map;

/**
 * Abstract shareable document view.
 *
 * @param <T> the type of identifier.
 */
@MappedSuperclass
@SuppressWarnings("unused")
abstract class ACalculator<T extends Serializable> extends ADocument<T> implements Serializable {

    private Boolean shared;

    /**
     * Test if the current entity is shared with other groups.
     *
     * @return shared flag
     */
    public Boolean getShared() {
        return shared;
    }

    /**
     * @return the related input names/type-identifier
     */
    public abstract Map<String, Integer> getInputs();
}
