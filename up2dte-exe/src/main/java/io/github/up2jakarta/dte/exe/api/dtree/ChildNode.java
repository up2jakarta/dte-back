package io.github.up2jakarta.dte.exe.api.dtree;

import io.github.up2jakarta.dte.exe.api.Identifiable;

/**
 * DTree Child Node.
 *
 * @see DefaultNode
 * @see DecisionNode
 * @see ComputationNode
 */
public interface ChildNode extends Identifiable<Long> {

    /**
     * @return the parent node
     */
    Identifiable<Long> getParent();

    /**
     * @return the child depth in the tree
     */
    Integer getDepth();

    /**
     * @return the child order in parent node
     */
    Integer getOrder();

    /**
     * Indicates whether some {@code other} object is equal to this one.
     *
     * @param other the reference object with which to compare.
     * @return {@code true} if this object is same as {@code other} argument, otherwise return {@code false}.
     */
    boolean equals(Object other);

    /**
     * Returns a hash code value for the object.
     *
     * @return a hash code value for this object.
     */
    int hashCode();
}
