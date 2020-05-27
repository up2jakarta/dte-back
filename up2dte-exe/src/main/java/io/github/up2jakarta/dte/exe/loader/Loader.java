package io.github.up2jakarta.dte.exe.loader;

/**
 * Engine Object Loader from data storage.
 *
 * @param <T> the type of loaded objects.
 */
public interface Loader<T> {

    /**
     * Load an existing object identified by {@code id} from the data storage.
     *
     * @param id the object unique identifier
     * @return the found object, otherwise throw {@link LoadingException}.
     * @throws LoadingException when the object not found or an error has occurred while building the result.
     */
    T load(long id) throws LoadingException;
}
