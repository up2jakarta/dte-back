package io.github.up2jakarta.dte.exe.api;

import java.io.Serializable;

/**
 * Identifiable with {@link Number} identifier.
 *
 * @param <T> the type of identifier.
 */
public interface Identifiable<T extends Number> extends Serializable {

    /**
     * Return the unique identifier of the object.
     *
     * @return the unique identifier.
     * @see Computer
     * @see Decider
     */
    T getId();

}
