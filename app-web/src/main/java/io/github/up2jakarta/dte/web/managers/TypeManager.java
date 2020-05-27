package io.github.up2jakarta.dte.web.managers;

import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.views.VType;
import io.github.up2jakarta.dte.web.models.TypeModel;

import java.util.List;

/**
 * {@link Type} Manager.
 */
public interface TypeManager {

    /**
     * Create a list of types without details from the given source {@code list}.
     * Set only the following properties: id, label, name and description.
     *
     * @param list the list of managed entities
     * @return the converted types
     */
    List<TypeModel> convert(List<VType> list);

}
