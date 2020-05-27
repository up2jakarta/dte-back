package io.github.up2jakarta.dte.jpa.repositories;

import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.views.VType;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * {@link Type} repository.
 */
public interface TypeRepository {

    /**
     * Find an existing type by the specified {@code identifier}.
     *
     * @param id the identifier
     * @return the found entity, otherwise {@code null}.
     */
    Type find(int id);

    /**
     * Find an existing type by the specified {@code identifiers}.
     *
     * @param ids the type identifiers
     * @return the found entities.
     */
    List<Type> find(Set<Integer> ids);

    /**
     * Find types by the specified {@code groupId}.
     *
     * @param groupId the group identifier
     * @return found types.
     */
    List<VType> search(int groupId);

    /**
     * @return the default type.
     */
    Type getDefault();

    /**
     * Convert the given API {@code types} to EXE types.
     *
     * @param types the API typing
     * @return the mapped EXE typing.
     */
    Map<String, Class<?>> serialize(Map<String, Integer> types);

    /**
     * Convert the given Engine {@code types} to API types.
     *
     * @param types the Engine typing
     * @return the mapped API typing.
     */
    Map<String, Integer> deserialize(Map<String, Class<?>> types);
}
