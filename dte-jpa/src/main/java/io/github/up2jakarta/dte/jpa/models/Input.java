package io.github.up2jakarta.dte.jpa.models;

import io.github.up2jakarta.dte.jpa.entities.DocumentedType;
import io.github.up2jakarta.dte.jpa.validators.Variable;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * {@link Node} input typing.
 */
@SuppressWarnings({"unused"})
public class Input {

    @Variable
    private String variable;
    @NotNull
    private Integer typeId;
    @NotEmpty
    private String label;
    @NotEmpty
    private String description;
    @NotNull
    private Boolean optional;

    /**
     * Public constructor for Input.
     */
    public Input() {

    }

    /**
     * Public constructor for Input.
     *
     * @param code  the variable name
     * @param entry the entry document
     */
    public Input(final String code, final DocumentedType entry) {
        this.variable = code;
        this.typeId = entry.getType().getId();
        this.label = entry.getLabel();
        this.description = entry.getDescription();
        this.optional = entry.isOptional();
    }

    /**
     * @return the declared variable name.
     */
    public String getVariable() {
        return variable;
    }

    /**
     * Set the declared variable name.
     *
     * @param variable the name
     */
    public void setVariable(final String variable) {
        this.variable = variable;
    }

    /**
     * @return the declared variable type identifier.
     */
    public Integer getTypeId() {
        return typeId;
    }

    /**
     * Set the declared variable type identifier.
     *
     * @param typeId the type identifier
     */
    public void setTypeId(final Integer typeId) {
        this.typeId = typeId;
    }

    /**
     * @return the display label.
     */
    public String getLabel() {
        return label;
    }

    /**
     * Set the display label.
     *
     * @param label the display label
     */
    public void setLabel(final String label) {
        this.label = label;
    }

    /**
     * @return the full description.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Set the full description/documentation.
     *
     * @param description the full description
     */
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * Tells the engine if the current variable is optional or required.
     *
     * @return optional flag
     */
    public Boolean getOptional() {
        return optional;
    }

    /**
     * Should the engine check the availability/nullability of the current variable in the input context.
     *
     * @param optional optional flag
     */
    public void setOptional(final Boolean optional) {
        this.optional = optional;
    }
}
