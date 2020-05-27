package io.github.up2jakarta.dte.jpa.api;

/**
 * DTE Context typing outputs.
 *
 * @param <T> the concrete type
 */
public interface Output<T extends IType> extends Input<T>, io.github.up2jakarta.dte.exe.api.Output {

    /**
     * Tells the engine if should share the current output variable with other scripts or other groups.
     *
     * @return shared flag
     */
    @Override
    boolean isShared();

    /**
     * Should share the current output variable with other scripts or other groups.
     *
     * @param shared shared flag
     */
    void setShared(boolean shared);
}
