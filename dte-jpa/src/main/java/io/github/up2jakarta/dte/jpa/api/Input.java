package io.github.up2jakarta.dte.jpa.api;

/**
 * Typing input.
 *
 * @param <T> the concrete type
 */
public interface Input<T extends IType> extends Documented, io.github.up2jakarta.dte.exe.api.Input {

    /**
     * Should the engine check the availability/nullability of the current variable in the input context.
     *
     * @param optional optional flag
     */
    void setOptional(boolean optional);

    @Override
    T getType();

    /**
     * Set the type of the current variable.
     *
     * @param type the variable type
     */
    void setType(T type);

}
