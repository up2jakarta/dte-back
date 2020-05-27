package io.github.up2jakarta.dte.web.models;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

/**
 * API Template Model.
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({"id", "groupId", "name", "label", "description"})
public class TemplateModel extends TypeModel {
    // TODO defined functions
}
