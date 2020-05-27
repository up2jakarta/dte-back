package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.exe.api.Identifiable;

import java.util.Map;
import java.util.Set;

/**
 * DTE Group interface.
 */
// TODO versioning group, DTree, BTree, template, type
public interface IGroup extends Documented, Identifiable<Integer> {

    /**
     * @return the display icon.
     */
    String getIcon();

    /**
     * Set the display icon.
     *
     * @param icon the display icon
     */
    void setIcon(String icon);

    /**
     * @return the display color.
     */
    String getColor();

    /**
     * Set the display color .
     *
     * @param color the display color
     */
    void setColor(String color);

    /**
     * Return the common script templates for all sub-{@link Script} can use one as base script.
     *
     * @return the defined templates.
     */
    Set<? extends ITemplate> getTemplates();

    /**
     * Return the declared types should be shared with sub-groups and their related trees.
     *
     * @return the defined types
     */
    Set<? extends IType> getTypes();

    /**
     * Return the common typing inputs for all related trees or sub-group.
     * ie any tree or group inherit from this group has access to this inputs and can override them.
     *
     * @return the typing inputs.
     */
    Map<String, ? extends Input> getTyping();

    /**
     * Return the common typing outputs for all related trees or sub-group.
     * ie any tree or group inherit from this group has access to this outputs and can override them.
     *
     * @return the typing outputs.
     */
    Map<String, ? extends Output> getDeclaredTyping();

    /**
     * Return the defined parameters should be shared with sub-groups and their related trees.
     *
     * @return the defined parameters
     */
    Map<String, ? extends IParameter> getParameters();
}
