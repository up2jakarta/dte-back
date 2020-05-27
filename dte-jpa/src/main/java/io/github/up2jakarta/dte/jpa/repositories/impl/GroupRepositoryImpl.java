package io.github.up2jakarta.dte.jpa.repositories.impl;

import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.grp.MainGroup;
import io.github.up2jakarta.dte.jpa.repositories.GroupRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.up2jakarta.dte.jpa.entities.Group_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * {@link GroupRepository} implementation.
 */
@Repository
public class GroupRepositoryImpl extends Util implements GroupRepository {

    private final EntityManager entityManager;
    private final int mainId;

    /**
     * Public constructor for GroupRepositoryImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public GroupRepositoryImpl(final EntityManager entityManager) {
        this.entityManager = entityManager;
        final CriteriaQuery<Integer> query = entityManager.getCriteriaBuilder().createQuery(Integer.class);
        final Root<MainGroup> root = query.from(MainGroup.class);
        final List<Integer> ids = entityManager.createQuery(query.multiselect(root.get(Group_.id))).getResultList();
        if (ids.isEmpty()) {
            throw new PersistenceException("Cannot find the technical group");
        }
        if (ids.size() != 1) {
            throw new PersistenceException("Many technical groups found: " + ids.size());
        }
        mainId = ids.get(0);
    }

    @Override
    public Group find(final int parentId, final String label) {
        if (label == null) {
            return null;
        }
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Group> query = builder.createQuery(Group.class);
        final Root<Group> root = query.from(Group.class);
        query.select(root).where(
                builder.equal(root.get(Group_.label), label),
                builder.equal(root.get(Group_.parent), parentId)
        );
        return first(entityManager.createQuery(query));
    }

    @Override
    public Group find(final int id) {
        return entityManager.find(Group.class, id);
    }

    @Override
    public Group merge(final Group entity) {
        return entityManager.merge(entity);
    }

    @Override
    public void persist(final Group entity) {
        entityManager.persist(entity);
    }

    @Override
    public void remove(final Group entity) {
        entityManager.remove(entity);
    }

    @Override
    public List<? extends Group> search(final Integer parentId) {
        final int pid = (parentId != null) ? parentId : mainId;
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Group> query = builder.createQuery(Group.class);
        final Root<Group> root = query.from(Group.class);
        query.select(root)
                .where(builder.equal(root.get(Group_.parent), pid))
                .orderBy(builder.asc(root.get(Group_.label)));
        return entityManager.createQuery(query).getResultList();
    }

    @Override
    public MainGroup getMain() {
        return entityManager.getReference(MainGroup.class, mainId);
    }

    @Override
    public List<? extends Group> tree() {
        final CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Group> query = builder.createQuery(Group.class);
        final Root<Group> root = query.from(Group.class);
        query.select(root).orderBy(builder.asc(root.get(Group_.parent)), builder.asc(root.get(Group_.label)));
        return entityManager.createQuery(query).getResultList();
    }
}
