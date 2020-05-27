package io.github.up2jakarta.dte.exe.loader;

import io.github.up2jakarta.dte.exe.api.Identifiable;

/**
 * Repository Finder.
 *
 * @param <T> the type of identifiable.
 */
public interface Finder<T extends Identifiable<Long>> {

    /**
     * Find an existing identifiable by unique identifier.
     *
     * @param id the identifier
     * @return the found identifiable, otherwise {@code null}.
     */
    T find(long id);
}
