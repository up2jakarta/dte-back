package io.github.up2jakarta.dte.exe.api;

/**
 * DTE Context typing outputs.
 */
public interface Output extends Input {

    /**
     * Tells the engine if should share the current output variable with other scripts.
     *
     * @return shared flag
     */
    boolean isShared();
}
