package io.github.up2jakarta.dte.jpa.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.entities.btn.*;
import io.github.up2jakarta.dte.jpa.validators.BinaryTree;

import javax.validation.Valid;
import java.util.List;

/**
 * Decider/Binary Tree Model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "id", "type", "label", "description", "script", "negated", "shared",
        "templateId", "deciderId", "groupId", "operator", "operands"
})
public class Decider extends Node<Decider> {

    private Type type;
    private Operator operator;
    private List<@Valid @BinaryTree(types = {Type.LOCAL, Type.DECIDER, Type.OPERATOR}) Decider> operands;

    /**
     * Protected constructor for Decider.
     */
    protected Decider() {
    }

    /**
     * Public constructor for Decider.
     *
     * @param type the node type.
     */
    public Decider(final Type type) {
        this.type = type;
    }

    /**
     * @return the node type.
     */
    public Type getType() {
        return type;
    }

    /**
     * Set the decider type.
     *
     * @param type the node type.
     */
    public void setType(final Type type) {
        this.type = type;
    }

    /**
     * @return the operands operator.
     */
    public Operator getOperator() {
        return operator;
    }

    /**
     * Set the children operands operator.
     *
     * @param operator the operator
     */
    public void setOperator(final Operator operator) {
        this.operator = operator;
    }

    /**
     * @return the list of children operands.
     */
    public List<Decider> getOperands() {
        return operands;
    }

    /**
     * Set the list of operands as a parent node.
     *
     * @param operands the list of operands
     */
    public final void setOperands(final List<Decider> operands) {
        this.operands = operands;
        if (operands != null) {
            operands.forEach(c -> c.parent = this);
        }
    }

    /**
     * Decider node type enumeration.
     */
    public enum Type {

        /**
         * {@link BTreePlainDecider}.
         */
        PLAIN(BTreePlainDecider.class),

        /**
         * {@link BTreeMixedDecider}.
         */
        MIXED(BTreeMixedDecider.class),

        /**
         * {@link BTreeOperatorNode}.
         */
        OPERATOR(BTreeOperatorNode.class),

        /**
         * {@link BTreeDeciderNode}.
         */
        DECIDER(BTreeDeciderNode.class),

        /**
         * {@link BTreeLocalNode}.
         */
        LOCAL(BTreeLocalNode.class);

        private final Class<? extends BTreeNode> entityClass;

        /**
         * Private constructor for Type.
         *
         * @param entityClass the entity class
         */
        Type(final Class<? extends BTreeNode> entityClass) {
            this.entityClass = entityClass;
        }

        /**
         * @return the related entity class.
         */
        public Class<? extends BTreeNode> getEntityClass() {
            return entityClass;
        }
    }

}
