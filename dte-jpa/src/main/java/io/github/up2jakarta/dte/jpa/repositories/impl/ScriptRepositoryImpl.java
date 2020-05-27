package io.github.up2jakarta.dte.jpa.repositories.impl;

import io.github.up2jakarta.dte.jpa.repositories.ScriptRepository;
import io.github.up2jakarta.dte.jpa.views.Key;
import io.github.up2jakarta.dte.jpa.views.VCScript;
import io.github.up2jakarta.dte.jpa.views.VDScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import io.github.up2jakarta.dte.jpa.views.*;

import javax.persistence.EntityGraph;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * {@link ScriptRepository} implementation.
 */
@Repository
public class ScriptRepositoryImpl extends Util implements ScriptRepository {

    private final EntityManager manager;

    /**
     * Public constructor for ScriptRepositoryImpl.
     *
     * @param entityManager the persistence context
     */
    @Autowired
    public ScriptRepositoryImpl(final EntityManager entityManager) {
        this.manager = entityManager;
    }

    @Override
    public VDScript find(final Key id) {
        final EntityGraph<VDScript> graph = manager.createEntityGraph(VDScript.class);
        graph.addAttributeNodes(VDScript_.inputs.getName());
        final Map<String, Object> properties = Collections.singletonMap(HINT_FETCH_GRAPH, graph);
        return manager.find(VDScript.class, id, properties);
    }

    @Override
    public List<VDScript> search(final int groupId, final List<String> inputs) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<VDScript> query = cb.createQuery(VDScript.class);
        final Root<VDScript> root = query.from(VDScript.class);
        final Predicate filter = cb.and(
                cb.or(
                        cb.isMember(groupId, root.get(VDScript_.GROUPS_FILTER)),
                        cb.isTrue(root.get(VDScript_.shared))
                ),
                containsAll(cb, root.get(VDScript_.INPUTS_FILTER), inputs)
        );
        query.select(root).where(filter).orderBy(cb.asc(root.get(VDScript_.label)));
        return manager.createQuery(query.distinct(true)).getResultList();
    }

    @Override
    public VCScript find(final long id) {
        final EntityGraph<VCScript> graph = manager.createEntityGraph(VCScript.class);
        graph.addAttributeNodes(VCScript_.inputs.getName(), VCScript_.outputs.getName());
        final Map<String, Object> properties = Collections.singletonMap(HINT_FETCH_GRAPH, graph);
        return manager.find(VCScript.class, id, properties);
    }

    @Override
    public List<VCScript> search(final int groupId, final List<String> inputs, final List<String> outputs) {
        final CriteriaBuilder cb = manager.getCriteriaBuilder();
        final CriteriaQuery<VCScript> query = cb.createQuery(VCScript.class);
        final Root<VCScript> root = query.from(VCScript.class);
        final Predicate filter = cb.and(
                cb.or(
                        cb.isMember(groupId, root.get(VCScript_.GROUPS_FILTER)),
                        cb.isTrue(root.get(VCScript_.shared))
                ),
                containsAll(cb, root.get(VCScript_.INPUTS_FILTER), inputs),
                containsAll(cb, root.get(VCScript_.OUTPUTS_FILTER), outputs)
        );
        query.select(root).where(filter).orderBy(cb.asc(root.get(VCScript_.label)));
        return manager.createQuery(query.distinct(true)).getResultList();
    }
}
