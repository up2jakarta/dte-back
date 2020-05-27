package io.github.up2jakarta.dte.web.managers.impl;

import org.springframework.stereotype.Component;
import io.github.up2jakarta.dte.jpa.views.VTemplate;
import io.github.up2jakarta.dte.web.managers.TemplateManager;
import io.github.up2jakarta.dte.web.models.TemplateModel;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * {@link TemplateManager} implementation.
 */
@Component
public class TemplateManagerImpl implements TemplateManager {

    @Override
    public List<TemplateModel> convert(final List<VTemplate> list) {
        return list.stream()
                .map(e -> {
                    final TemplateModel m = new TemplateModel();
                    m.setId(e.getId());
                    m.setName(e.getName());
                    m.setLabel(e.getLabel());
                    m.setDescription(e.getDescription());
                    return m;
                })
                .collect(toList());
    }
}
