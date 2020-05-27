package io.github.up2jakarta.dte.exe.api;

import io.github.up2jakarta.dte.exe.script.ContextException;

/**
 * DTE Context typing inputs.
 */
public interface Input {

    /**
     * Tells the engine if the current variable is optional or required.
     * The engine maye throw {@link ContextException} when required
     * variables are missing in the execution input context.
     *
     * @return optional flag
     */
    boolean isOptional();

    /**
     * Return the type of the current variable.
     *
     * @return the type.
     */
    Type getType();
}
