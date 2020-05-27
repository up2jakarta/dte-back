package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.web.models.TemplateModel;

import java.util.List;

/**
 * Service responsible for templates management.
 */
public interface TemplateService {

    /**
     * Search declared templates in the specified group identifier.
     *
     * @param groupId the group identifier
     * @return the found templates
     */
    List<TemplateModel> search(int groupId);

}
