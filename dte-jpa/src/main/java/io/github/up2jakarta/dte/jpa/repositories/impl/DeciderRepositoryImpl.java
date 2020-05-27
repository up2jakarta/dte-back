package io.github.up2jakarta.dte.jpa.repositories.impl;

import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.repositories.DeciderRepository;
import io.github.up2jakarta.dte.jpa.views.VDecider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider_;
import io.github.up2jakarta.dte.jpa.views.VDecider_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * {@link DeciderRepository}  implementation.
 */
@Repository
//TODO loader eviction
public class DeciderRepositoryImpl extends Util implements DeciderRepository {

    private final EntityManager manager;

    /**
     * Public constructor for DeciderRepositoryImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public DeciderRepositoryImpl(final EntityManager entityManager) {
        this.manager = entityManager;
    }

    @Override
    public BTreeDecider find(final int groupId, final String label) {
        if (label == null) {
            return null;
        }
        final CriteriaBuilder builder = manager.getCriteriaBuilder();
        final CriteriaQuery<BTreeDecider> query = builder.createQuery(BTreeDecider.class);
        final Root<BTreeDecider> root = query.from(BTreeDecider.class);
        query.select(root).where(
                builder.equal(root.get(BTreeDecider_.group), groupId),
                builder.equal(root.get(BTreeDecider_.label), label)
        );
        return first(manager.createQuery(query));
    }

    @Override
    public BTreeDecider find(final long id) {
        final TypedQuery<BTreeDecider> query = manager.createNamedQuery("BTN_FIND_BY_ID", BTreeDecider.class)
                .setParameter("id", id);
        return first(query);
    }

    @Override
    public void persist(final BTreeDecider decider) {
        manager.persist(decider);
    }

    @Override
    public <T extends BTreeDecider> T merge(final T decider) {
        return manager.merge(decider);
    }

    @Override
    public void remove(final BTreeDecider decider) {
        manager.remove(decider);
    }

    @Override
    public List<VDecider> search(final int groupId, final List<String> inputs) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<VDecider> query = cb.createQuery(VDecider.class);
        final Root<VDecider> root = query.from(VDecider.class);
        final Predicate filter = cb.and(
                cb.equal(root.get(VDecider_.GROUP_ID), groupId),
                containsAll(cb, root.get(VDecider_.INPUTS_FILTER), inputs)
        );
        query.select(root).where(filter).orderBy(cb.asc(root.get(VDecider_.label)));
        return manager.createQuery(query.distinct(true)).getResultList();
    }
}
