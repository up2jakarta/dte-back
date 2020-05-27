package io.github.up2jakarta.dte.jpa.repositories.impl;

import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.repositories.ComputerRepository;
import io.github.up2jakarta.dte.jpa.views.VComputer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer_;
import io.github.up2jakarta.dte.jpa.views.VComputer_;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * {@link ComputerRepository}  implementation.
 */
@Repository
//TODO loader eviction
public class ComputerRepositoryImpl extends Util implements ComputerRepository {

    private final EntityManager manager;

    /**
     * Public constructor for ComputerRepositoryImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public ComputerRepositoryImpl(final EntityManager entityManager) {
        this.manager = entityManager;
    }

    @Override
    public DTreeComputer find(final int groupId, final String label) {
        if (label == null) {
            return null;
        }
        final CriteriaBuilder builder = manager.getCriteriaBuilder();
        final CriteriaQuery<DTreeComputer> query = builder.createQuery(DTreeComputer.class);
        final Root<DTreeComputer> root = query.from(DTreeComputer.class);
        query.select(root).where(
                builder.equal(root.get(DTreeComputer_.group), groupId),
                builder.equal(root.get(DTreeComputer_.label), label)
        );
        return first(manager.createQuery(query));
    }

    @Override
    public DTreeComputer find(final long id) {
        final TypedQuery<DTreeComputer> query = manager.createNamedQuery("DTN_FIND_BY_ID", DTreeComputer.class)
                .setParameter("id", id);
        return first(query);
    }

    @Override
    public void remove(final DTreeComputer computer) {
        manager.remove(computer);
    }

    @Override
    public <T extends DTreeComputer> T merge(final T computer) {
        return manager.merge(computer);
    }

    @Override
    public void persist(final DTreeComputer computer) {
        manager.persist(computer);
    }

    @Override
    public List<VComputer> search(final int groupId, final List<String> inputs, final List<String> outputs) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<VComputer> query = cb.createQuery(VComputer.class);
        final Root<VComputer> root = query.from(VComputer.class);
        final Predicate filter = cb.and(
                cb.equal(root.get(VComputer_.GROUP_ID), groupId),
                containsAll(cb, root.get(VComputer_.INPUTS_FILTER), inputs),
                containsAll(cb, root.get(VComputer_.OUTPUTS_FILTER), outputs)
        );
        query.select(root).where(filter).orderBy(cb.asc(root.get(VComputer_.label)));
        return manager.createQuery(query.distinct(true)).getResultList();
    }

}
