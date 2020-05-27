package io.github.up2jakarta.dte.exe.loader;

/**
 * Engine Loader from data storage with cache management.
 *
 * @param <T> the type of loaded objects.
 */
@SuppressWarnings("WeakerAccess")
public interface CacheLoader<T> extends Loader<T>, CacheRegion {
}
