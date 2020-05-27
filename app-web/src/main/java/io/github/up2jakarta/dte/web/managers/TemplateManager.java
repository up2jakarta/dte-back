package io.github.up2jakarta.dte.web.managers;

import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.views.VTemplate;
import io.github.up2jakarta.dte.web.models.TemplateModel;

import java.util.List;

/**
 * {@link Template} Manager.
 */
public interface TemplateManager {

    /**
     * Create a list of templates without details from the given source {@code list}.
     * Set only the following properties: id, label, name and description.
     *
     * @param list the list of managed entities
     * @return the converted templates
     */
    List<TemplateModel> convert(List<VTemplate> list);

}
