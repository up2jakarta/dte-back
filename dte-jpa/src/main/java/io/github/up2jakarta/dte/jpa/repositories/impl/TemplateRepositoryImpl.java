package io.github.up2jakarta.dte.jpa.repositories.impl;

import io.github.up2jakarta.dte.jpa.entities.Template;
import io.github.up2jakarta.dte.jpa.entities.tpl.BasicTemplate;
import io.github.up2jakarta.dte.jpa.repositories.TemplateRepository;
import io.github.up2jakarta.dte.jpa.views.VTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.jpa.entities.tpl.BasicTemplate_;
import io.github.up2jakarta.dte.jpa.views.VTemplate_;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceException;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * {@link TemplateRepository} implementation.
 */
@Repository
public class TemplateRepositoryImpl extends Util implements TemplateRepository {

    private final EntityManager manager;
    private final int defaultId;

    /**
     * Public constructor for GroupRepositoryImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public TemplateRepositoryImpl(final EntityManager entityManager) {
        this.manager = entityManager;
        final CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        final CriteriaQuery<Integer> query = cb.createQuery(Integer.class);
        final Root<BasicTemplate> root = query.from(BasicTemplate.class);
        query.multiselect(root.get(BasicTemplate_.id)).where(cb.equal(root.get(BasicTemplate_.base), BaseScript.class));
        final Integer id = first(entityManager.createQuery(query));
        if (id == null) {
            throw new PersistenceException("Cannot find the base template");
        }
        defaultId = id;
    }

    @Override
    public Template find(final int id) {
        return manager.find(Template.class, id);
    }

    @Override
    public List<VTemplate> search(final int groupId) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<VTemplate> query = cb.createQuery(VTemplate.class);
        final Root<VTemplate> root = query.from(VTemplate.class);
        final Predicate filter = cb.isMember(groupId, root.get(VTemplate_.GROUPS_FILTER));
        query.select(root).where(filter).orderBy(cb.asc(root.get(VTemplate_.label)));
        return manager.createQuery(query.distinct(true)).getResultList();
    }

    @Override
    public Template getDefault() {
        return manager.getReference(BasicTemplate.class, defaultId);
    }

}
