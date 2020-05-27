package io.github.up2jakarta.dte.web.managers.impl;

import org.springframework.stereotype.Component;
import io.github.up2jakarta.dte.jpa.views.VType;
import io.github.up2jakarta.dte.web.managers.TypeManager;
import io.github.up2jakarta.dte.web.models.TypeModel;

import java.util.List;

import static java.util.stream.Collectors.toList;

/**
 * {@link TypeManager} implementation.
 */
@Component
public class TypeManagerImpl implements TypeManager {

    @Override
    public List<TypeModel> convert(final List<VType> list) {
        return list.stream()
                .map(e -> {
                    final TypeModel m = new TypeModel();
                    m.setId(e.getId());
                    m.setName(e.getName());
                    m.setLabel(e.getLabel());
                    m.setDescription(e.getDescription());
                    return m;
                })
                .collect(toList());
    }
}
