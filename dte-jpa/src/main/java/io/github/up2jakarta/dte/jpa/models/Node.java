package io.github.up2jakarta.dte.jpa.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.jpa.api.Documented;

import javax.validation.Valid;
import java.util.List;

/**
 * Common Tree Node for {@link Computer} and {@link Decider}.
 *
 * @param <T> the concrete type
 */
public abstract class Node<T extends Node<T>> implements Documented, Identifiable<Long> {

    protected T parent;
    private Long id;
    private String label;
    private String description;
    private String script;
    private Boolean negated;
    private Boolean shared;
    private Integer groupId;
    private Integer templateId;
    private Long deciderId;
    private List<@Valid Input> inputs;

    @Override
    public Long getId() {
        return id;
    }

    /**
     * Set the node identifier.
     *
     * @param id the identifier
     */
    public void setId(final Long id) {
        this.id = id;
    }

    @Override
    public String getLabel() {
        return label;
    }

    @Override
    public void setLabel(final String label) {
        this.label = label;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String desc) {
        this.description = desc;
    }

    /**
     * @return the script source code.
     */
    public String getScript() {
        return script;
    }

    /**
     * Set the script source code.
     *
     * @param script the script source code
     */
    public void setScript(final String script) {
        this.script = script;
    }

    /**
     * Return a flag that tells the engine to negate the boolean result after evaluation.
     *
     * @return the indicator flag
     */
    public Boolean getNegated() {
        return negated;
    }

    /**
     * Tells the engine to negate the boolean result after evaluation.
     *
     * @param negated the flag
     */
    public void setNegated(final Boolean negated) {
        this.negated = negated;
    }

    /**
     * Return a flag that tells the engine to share this tree with other groups.
     *
     * @return the indicator flag
     */
    public Boolean getShared() {
        return shared;
    }

    /**
     * Tells the engine to share this tree with other groups.
     *
     * @param shared the flag
     */
    public void setShared(final Boolean shared) {
        this.shared = shared;
    }

    /**
     * @return the group identifier.
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Set the related group identifier.
     *
     * @param groupId the group identifier
     */
    public final void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the script template identifier
     */
    public Integer getTemplateId() {
        return templateId;
    }

    /**
     * Set the script base template through its identifier, the compiled script will inherit from this template.
     *
     * @param templateId the script template identifier
     */
    public void setTemplateId(final Integer templateId) {
        this.templateId = templateId;
    }

    /**
     * @return the linked decider identifier.
     */
    public Long getDeciderId() {
        return deciderId;
    }

    /**
     * Set the linked decider identifier.
     *
     * @param deciderId the decider identifier
     */
    public void setDeciderId(final Long deciderId) {
        this.deciderId = deciderId;
    }

    /**
     * @return the context input typing.
     */
    public List<Input> getInputs() {
        return inputs;
    }

    /**
     * Set the context input typing.
     *
     * @param inputs the typing
     */
    public void setInputs(final List<Input> inputs) {
        this.inputs = inputs;
    }

    /**
     * @return the parent node for child node, otherwise {@code null}.
     */
    @JsonIgnore
    public T getParent() {
        return parent;
    }
}
