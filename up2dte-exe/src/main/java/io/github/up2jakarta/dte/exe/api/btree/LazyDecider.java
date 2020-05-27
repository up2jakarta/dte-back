package io.github.up2jakarta.dte.exe.api.btree;

import io.github.up2jakarta.dte.exe.api.Decider;

/**
 * Decider wrapper, useful for lazy loading.
 */
public interface LazyDecider {

    /**
     * Return the wrapper decider, i.e the decider which is related to.
     *
     * @return the foreign decider
     */
    Decider getDecider();
}
