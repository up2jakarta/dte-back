package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.views.VTemplate;

import java.util.List;

/**
 * {@link Template} repository.
 */
public interface TemplateRepository {

    /**
     * Find an existing type by identifier.
     *
     * @param id the identifier
     * @return the found entity, otherwise {@code null}.
     */
    Template find(int id);

    /**
     * Find templates by the specified {@code groupId}.
     *
     * @param groupId the group identifier
     * @return found types.
     */
    List<VTemplate> search(int groupId);

    /**
     * @return the default template.
     */
    Template getDefault();
}
