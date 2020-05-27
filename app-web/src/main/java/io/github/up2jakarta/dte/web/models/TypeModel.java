package io.github.up2jakarta.dte.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import io.github.up2jakarta.dte.exe.api.Identifiable;
import io.github.up2jakarta.dte.jpa.api.Documented;
import io.github.up2jakarta.dte.web.markers.Post;
import io.github.up2jakarta.dte.web.markers.Put;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

/**
 * API Type Model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "groupId", "name", "label", "description"})
@SuppressWarnings("unused")
public class TypeModel implements Documented, Identifiable<Integer> {

    private Integer id;
    @NotNull(groups = Post.class)
    private Integer groupId;
    @NotEmpty(groups = {Post.class, Put.class})
    private String name;
    @NotEmpty(groups = {Post.class, Put.class})
    private String label;
    @NotEmpty(groups = {Post.class, Put.class})
    private String description;

    @Override
    public Integer getId() {
        return id;
    }

    /**
     * Set the type identifier.
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

    @Override
    public String getDescription() {
        return description;
    }

    @Override
    public void setDescription(final String description) {
        this.description = description;
    }

    /**
     * @return the related group identifier.
     */
    public Integer getGroupId() {
        return groupId;
    }

    /**
     * Set the related group identifier.
     *
     * @param groupId the related group identifier
     */
    public void setGroupId(final Integer groupId) {
        this.groupId = groupId;
    }

    /**
     * @return the server side class name.
     */
    public String getName() {
        return name;
    }

    /**
     * Set the server side class name.
     *
     * @param name the class full name
     */
    public void setName(final String name) {
        this.name = name;
    }
}
