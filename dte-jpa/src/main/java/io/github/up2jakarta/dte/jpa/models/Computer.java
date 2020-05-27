package io.github.up2jakarta.dte.jpa.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.up2jakarta.dte.jpa.entities.dtn.*;
import io.github.up2jakarta.dte.jpa.validators.DecisionTree;

import javax.validation.Valid;
import java.util.List;

import static io.github.up2jakarta.dte.jpa.models.Computer.Type.*;

/**
 * Computer/Decision Tree Model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id", "type", "label", "description", "script", "negated", "shared",
        "templateId", "deciderId", "groupId", "computerId", "inputs", "outputs", "children"
})
public class Computer extends Node<Computer> {

    private Type type;
    private Long computerId;
    private List<@Valid @DecisionTree(types = {LOCAL, COMPUTER, DEFAULT, DECIDER, DECISION}) Computer> children;
    private List<@Valid Output> outputs;

    /**
     * Protected constructor for Computer.
     */
    protected Computer() {
    }

    /**
     * Public constructor for Computer.
     *
     * @param type the node type.
     */
    public Computer(final Type type) {
        this.type = type;
    }

    /**
     * @return the node type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the computer type.
     *
     * @param type the node type.
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * @return the linked computer identifier.
     */
    public Long getComputerId() {
        return computerId;
    }

    /**
     * Set the linked computer identifier.
     *
     * @param computerId the computer identifier
     */
    public void setComputerId(final Long computerId) {
        this.computerId = computerId;
    }

    /**
     * @return the list of children nodes.
     */
    public List<Computer> getChildren() {
        return children;
    }

    /**
     * Set the list of children nodes as a parent node.
     *
     * @param children the list of children nodes
     */
    public final void setChildren(final List<Computer> children) {
        this.children = children;
        if (children != null) {
            children.forEach(c -> c.parent = this);
        }
    }

    /**
     * @return the context output typing.
     */
    public List<Output> getOutputs() {
        return outputs;
    }

    /**
     * Set the context output typing.
     *
     * @param outputs the typing
     */
    public void setOutputs(final List<Output> outputs) {
        this.outputs = outputs;
    }

    /**
     * Computer node type enumeration.
     */
    public enum Type {

        /**
         * {@link DTreePlainComputer}.
         */
        PLAIN(false, DTreePlainComputer.class),

        /**
         * {@link DTreeMixedComputer}.
         */
        MIXED(false, DTreeMixedComputer.class),

        /**
         * {@link DTreeComputerNode}.
         */
        COMPUTER(false, DTreeComputerNode.class),

        /**
         * {@link DTreeLocalNode}.
         */
        LOCAL(false, DTreeLocalNode.class),

        /**
         * {@link DTreeDefaultNode}.
         */
        DEFAULT(true, DTreeDefaultNode.class),

        /**
         * {@link DTreeDeciderNode}.
         */
        DECIDER(true, DTreeDeciderNode.class),

        /**
         * {@link DTreeDecisionNode}.
         */
        DECISION(true, DTreeDecisionNode.class);

        private final boolean isDecision;
        private final Class<? extends DTreeNode> entityClass;

        /**
         * Private constructor for Type.
         *
         * @param isDecision  is a decision node
         * @param entityClass the entity class
         */
        Type(final boolean isDecision, final Class<? extends DTreeNode> entityClass) {
            this.isDecision = isDecision;
            this.entityClass = entityClass;
        }

        /**
         * @return the related entity class.
         */
        public Class<? extends DTreeNode> getEntityClass() {
            return entityClass;
        }

        /**
         * @return is a decision node.
         */
        public boolean isDecision() {
            return isDecision;
        }
    }

}
