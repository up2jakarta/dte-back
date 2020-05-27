package io.github.up2jakarta.dte.jpa.repositories.impl;

import io.github.up2jakarta.dte.exe.loader.LoadingException;
import io.github.up2jakarta.dte.jpa.entities.Type;
import io.github.up2jakarta.dte.jpa.entities.tpe.BasicType;
import io.github.up2jakarta.dte.jpa.repositories.GroupRepository;
import io.github.up2jakarta.dte.jpa.repositories.TypeRepository;
import io.github.up2jakarta.dte.jpa.views.VType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.up2jakarta.dte.jpa.entities.Type_;
import io.github.up2jakarta.dte.jpa.entities.grp.AbstractGroup_;
import io.github.up2jakarta.dte.jpa.entities.tpe.BasicType_;
import io.github.up2jakarta.dte.jpa.views.VType_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * {@link TypeRepository} implementation.
 */
@Repository
public class TypeRepositoryImpl extends Util implements TypeRepository {

    private static volatile Map<Class, Integer> cache;
    private static volatile Map<Integer, Class> reverseCache;
    private final int defaultId;
    private final EntityManager manager;

    /**
     * Public constructor for TypeRepositoryImpl.
     *
     * @param entityManager   the persistence context
     * @param groupRepository the group repository
     */
    @Autowired
    public TypeRepositoryImpl(final EntityManager entityManager, final GroupRepository groupRepository) {
        this.manager = entityManager;
        this.init(manager, groupRepository.getMain().getId());
        final Integer id = cache.get(Object.class);
        if (id == null) {
            throw new PersistenceException("Cannot find the base type");
        }
        defaultId = id;
    }

    private void init(final EntityManager manager, final int groupId) {
        if (cache == null) {
            synchronized (TypeRepository.class) {
                if (cache == null) {
                    final CriteriaBuilder cb = manager.getCriteriaBuilder();
                    final CriteriaQuery<BasicType> query = cb.createQuery(BasicType.class);
                    final Root<BasicType> root = query.from(BasicType.class);
                    query.where(cb.equal(root.get(BasicType_.GROUP).get(AbstractGroup_.ID), groupId));
                    final List<BasicType> types = manager.createQuery(query).getResultList();
                    cache = types.stream().collect(Collectors.toConcurrentMap(Type::getJavaType, Type::getId));
                    reverseCache = types.stream().collect(Collectors.toConcurrentMap(Type::getId, Type::getJavaType));
                }
            }
        }
    }

    @Override
    public Type find(final int id) {
        return manager.find(Type.class, id);
    }

    @Override
    public List<VType> search(final int groupId) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<VType> query = cb.createQuery(VType.class);
        final Root<VType> root = query.from(VType.class);
        final Predicate filter = cb.isMember(groupId, root.get(VType_.GROUPS_FILTER));
        query.select(root).where(filter).orderBy(cb.asc(root.get(VType_.label)));
        return manager.createQuery(query.distinct(true)).getResultList();
    }

    @Override
    public Type getDefault() {
        return manager.getReference(BasicType.class, defaultId);
    }

    @Override
    public Map<String, Class<?>> serialize(final Map<String, Integer> types) {
        final Map<String, Class<?>> typing = new HashMap<>(types.size());
        final Map<String, Integer> remaining = new HashMap<>(types.size());
        types.forEach((k, v) -> {
            final Class<?> type = reverseCache.get(v);
            if (type != null) {
                typing.put(k, type);
            } else {
                remaining.put(k, v);
            }
        });
        if (!remaining.isEmpty()) {
            final Map<Integer, Class> store = find(new HashSet<>(remaining.values())).stream()
                    .collect(Collectors.toMap(Type::getId, Type::getJavaType));
            final Function<Integer, Class<?>> converter = (d) -> {
                final Class<?> type = store.get(d);
                if (type == null) {
                    throw new LoadingException("type", d);
                }
                return type;
            };
            remaining.forEach((k, t) -> typing.put(k, converter.apply(t)));
        }
        return typing;
    }

    @Override
    public Map<String, Integer> deserialize(final Map<String, Class<?>> types) {
        final Map<String, Integer> typing = new HashMap<>(types.size());
        types.forEach((k, v) -> typing.put(k, cache.getOrDefault(v, extractId(v))));
        return typing;
    }

    @Override
    public List<Type> find(final Set<Integer> ids) {
        final CriteriaQuery<Type> query = manager.getCriteriaBuilder().createQuery(Type.class);
        final Root<Type> root = query.from(Type.class);
        query.where(root.get(Type_.id).in(ids));
        return manager.createQuery(query).getResultList();
    }

    @SuppressWarnings("unused")
    private Integer extractId(final Class<?> type) {
        // TODO read id from the generated class
        //      Add Annotation(id, version) for generated Classes
        //      IF unknown create new type or defaultId
        return defaultId;
    }

}
