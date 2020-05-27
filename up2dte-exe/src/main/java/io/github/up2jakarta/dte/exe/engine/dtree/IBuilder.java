package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.DTree;

/**
 * Empty build, i.e no operations build except the build operations.
 */
public interface IBuilder {

    /**
     * Build the engine calculation and returns it.
     *
     * @return the built-in Calculation.
     */
    DTree build();
}
