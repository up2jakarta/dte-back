package io.github.up2jakarta.dte.jpa.api.dtree;

import io.github.up2jakarta.dte.exe.api.Identifiable;

/**
 * Decision tree node.
 */
public interface INode extends Identifiable<Long> {

    /**
     * @return the display label.
     */
    String getLabel();

    /**
     * Set the decision node node display label.
     *
     * @param label the label
     */
    void setLabel(String label);

}
