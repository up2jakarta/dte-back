package io.github.up2jakarta.dte.exe.loader;

/**
 * Engine Cache Region specification.
 */
public interface CacheRegion {

    /**
     * Removes the object identified by {@code id} from the cache.
     *
     * @param id the object unique identifier
     */
    void evict(long id);


    /**
     * Evict all object from the cache.
     */
    void evict();
}
