package io.github.up2jakarta.dte.exe.api.dtree;

import io.github.up2jakarta.dte.exe.api.Computer;

/**
 * Computer wrapper, useful for lazy loading.
 */
public interface LazyComputer {

    /**
     * Return the wrapper computer, i.e the computer which is related to.
     *
     * @return the foreign computer
     */
    Computer getComputer();
}
