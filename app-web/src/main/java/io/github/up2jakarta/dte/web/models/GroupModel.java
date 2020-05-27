package io.github.up2jakarta.dte.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.jpa.entities.grp.WorkGroup;
import io.github.up2jakarta.dte.jpa.api.Documented;
import io.github.up2jakarta.dte.web.markers.Post;
import io.github.up2jakarta.dte.web.markers.Put;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * API Group Model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "label", "icon", "color", "description", "parentId"})
@SuppressWarnings("unused")
public class GroupModel implements Documented, Identifiable<Integer> {

    private Integer id;
    @NotNull(groups = Post.class)
    private Integer parentId;
    @NotEmpty(groups = {Post.class, Put.class})
    private String label;
    @NotEmpty(groups = {Post.class, Put.class})
    private String description;
    @NotEmpty(groups = {Post.class, Put.class})
    private String icon;
    private String color;
    private List<GroupModel> children;
    private boolean locked;

    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Set the group identifier.
     *
     * @param id the unique id
     */
    public void setId(final Integer id) {
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

    /**
     * @return the display icon.
     */
    public String getIcon() {
        return icon;
    }

    /**
     * Set the display icon.
     *
     * @param icon the display icon
     */
    public void setIcon(final String icon) {
        this.icon = icon;
    }

    /**
     * @return the display color.
     */
    public String getColor() {
        return color;
    }

    /**
     * Set the display color .
     *
     * @param color the display color
     */
    public void setColor(final String color) {
        this.color = color;
    }

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the parent group identifier.
     */
    public Integer getParentId() {
        return parentId;
    }

    /**
     * Set the parent group identifier.
     *
     * @param parentId the parent identifier
     */
    public void setParentId(final Integer parentId) {
        this.parentId = parentId;
    }

    /**
     * @return the children groups.
     */
    public List<GroupModel> getChildren() {
        return children;
    }

    /**
     * Add the given {@code child} group to the list of children.
     *
     * @param child the child to add
     */
    public void addChild(final GroupModel child) {
        if (children == null) {
            children = new ArrayList<>();
        }
        children.add(child);
    }

    /**
     * Test if can add sub groups to the current group.
     *
     * @return {@code true} if {@link WorkGroup}/
     */
    public boolean isLocked() {
        return locked;
    }

    /**
     * Lock the addition of sub groups to the current group.
     *
     * @param locked the locked flag
     */
    public void setLocked(final boolean locked) {
        this.locked = locked;
    }
}
