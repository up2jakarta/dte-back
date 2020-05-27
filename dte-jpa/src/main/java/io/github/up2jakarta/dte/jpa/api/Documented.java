package io.github.up2jakarta.dte.jpa.api;

import java.io.Serializable;

/**
 * Document tree{@link Node}.
 * Make the tree node shared in the same group and their children.
 */
public interface Documented extends Serializable {

    /**
     * @return the display label.
     */
    String getLabel();

    /**
     * Set the display label.
     *
     * @param label the label
     */
    void setLabel(String label);

    /**
     * @return the description or documentation.
     */
    String getDescription();

    /**
     * Set the group description or documentation.
     *
     * @param desc the description
     */
    void setDescription(String desc);
}
