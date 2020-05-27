package io.github.up2jakarta.dte.jpa.models;

import io.github.up2jakarta.dte.jpa.entities.ShareableType;

import javax.validation.constraints.NotNull;

/**
 * {@link Computer} output typing.
 */
@SuppressWarnings({"unused"})
public class Output extends Input {

    @NotNull
    private Boolean shared;

    /**
     * Public constructor for Output.
     */
    public Output() {
    }

    /**
     * Public constructor for Output.
     *
     * @param code  the variable name
     * @param entry the shareable entry
     */
    public Output(final String code, final ShareableType entry) {
        super(code, entry);
        this.shared = entry.isShared();
    }

    /**
     * Tells the engine if should share the current output variable with other scripts or other groups.
     *
     * @return shared flag
     */
    public Boolean getShared() {
        return shared;
    }

    /**
     * Should share the current output variable with other scripts or other groups.
     *
     * @param shared shared flag
     */
    public void setShared(final Boolean shared) {
        this.shared = shared;
    }
}
