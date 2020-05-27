package io.github.up2jakarta.dte.jpa.api.btree;

import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.exe.api.Negateable;

/**
 * Binary tree node.
 */
public interface INode extends Negateable, Identifiable<Long> {

    /**
     * @return the display label.
     */
    String getLabel();

    /**
     * Set the binary tree node display label.
     *
     * @param label the label
     */
    void setLabel(String label);

    /**
     * Set the negated flag.
     *
     * @param negated the negated flag
     */
    void setNegated(boolean negated);

}
