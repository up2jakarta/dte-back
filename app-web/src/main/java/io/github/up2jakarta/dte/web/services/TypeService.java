package io.github.up2jakarta.dte.web.services;

import io.github.up2jakarta.dte.web.models.TypeModel;

import java.util.List;

/**
 * Service responsible for types management.
 */
public interface TypeService {

    /**
     * Search declared types in the specified group identifier.
     *
     * @param groupId the group identifier
     * @return the found types
     */
    List<TypeModel> search(int groupId);

}
