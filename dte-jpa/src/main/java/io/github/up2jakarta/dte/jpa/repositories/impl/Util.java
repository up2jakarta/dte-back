package io.github.up2jakarta.dte.jpa.repositories.impl;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

/**
 * JPA Repository utility.
 */
@SuppressWarnings("unused")
abstract class Util {

    /**
     * JPA Hint that let the provider treats all attributes specified in your entity graph {@code FetchType.EAGER},
     * and all attributes not specified will be treated as {@code FetchType.LAZY}.
     */
    static final String HINT_FETCH_GRAPH = "javax.persistence.fetchgraph";

    /**
     * JPA Hint that let the provider treats all attributes specified in your entity graph {@code FetchType.EAGER},
     * but attributes not specified use their specified type or default if the entity specified nothing.
     */
    static final String HINT_LOAD_GRAPH = "javax.persistence.loadgraph ";

    /**
     * Returns safely the first result of the given {@code query} results.
     *
     * @param query the JPA Typed Query
     * @param <E>   the entity type
     * @return the first entity if found, otherwise {@code null}
     */
    <E> E first(final TypedQuery<E> query) {
        final List<E> results = query.getResultList();
        if (results.isEmpty()) {
            return null;
        }
        return results.get(0);
    }

    /**
     * Returns a {@link Predicate} that ensures that the given {@code path} contains all the given {@code values}.
     * If the values are empty, then returns {@code true} predicate.
     *
     * @param cb     the builder
     * @param path   the collection path
     * @param values the list of values
     * @param <T>    the type of values
     * @return a not null predicate
     */
    final <T extends Comparable<T>> Predicate containsAll(final CriteriaBuilder cb, final Path<Collection<T>> path,
                                                          final List<T> values) {
        if (values == null || values.isEmpty()) {
            return cb.isTrue(cb.literal(true));
        }
        final Predicate[] predicates = new Predicate[values.size()];
        final Iterator<T> it = values.iterator();
        for (int i = 0; it.hasNext(); i++) {
            predicates[i] = cb.isMember(it.next(), path);
        }
        return cb.and(predicates);
    }
}
