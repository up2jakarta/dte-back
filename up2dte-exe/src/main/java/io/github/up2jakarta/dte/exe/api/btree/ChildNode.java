package io.github.up2jakarta.dte.exe.api.btree;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.exe.api.Negateable;

/**
 * BTree Child Node.
 *
 * @see LeafNode
 * @see ParentNode
 */
public interface ChildNode extends Negateable, Identifiable<Long> {

    /**
     * @return the parent node
     */
    ParentNode getParent();

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
