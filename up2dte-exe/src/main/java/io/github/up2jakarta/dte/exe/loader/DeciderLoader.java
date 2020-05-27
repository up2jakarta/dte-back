package io.github.up2jakarta.dte.exe.loader;

import org.codehaus.groovy.runtime.memoize.EvictableCache;
import org.codehaus.groovy.runtime.memoize.UnlimitedConcurrentCache;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.btree.ChildNode;
import io.github.up2jakarta.dte.exe.api.btree.MixedDecider;
import io.github.up2jakarta.dte.exe.api.btree.PlainDecider;
import io.github.up2jakarta.dte.exe.engine.BTree;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.ConditionFactory;
import io.github.up2jakarta.dte.exe.engine.Decision;
import io.github.up2jakarta.dte.exe.engine.btree.Leaf;
import io.github.up2jakarta.dte.exe.engine.btree.Node;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Decider Loader implementation.
 *
 * @author A.ABBESSI
 */
public class DeciderLoader implements CacheLoader<Condition> {

    private static final EvictableCache<Long, Condition> CACHE = new UnlimitedConcurrentCache<>();

    private final Finder<Decider> finder;
    private final NodeParser<Node, ChildNode> parser;

    /**
     * Public constructor for DeciderLoader.
     *
     * @param finder the decider finder
     * @param engine the DTE engine
     */
    public DeciderLoader(final Finder<Decider> finder, final StaticEngine engine) {
        this.finder = finder;
        this.parser = new DeciderNodeParser(engine);
    }

    /**
     * Get the total count of cached deciders in this region.
     *
     * @return the size of the cache
     */
    public static long getCacheRegionSize() {
        return CACHE.size();
    }

    /**
     * Removes all dependencies of the given BTree {@code node} from the cache.
     *
     * @param node the boolean tree node
     */
    private void remove(final Node node) {
        if (node instanceof Leaf) {
            final Condition c = ((Leaf) node).getCondition();
            if (c instanceof CacheEntry) {
                ConditionFactory.evict(((CacheEntry) c).getId());
            }
        } else if (node instanceof Operand) {
            final Operand op = (Operand) node;
            for (final Iterator<Node> it = op.iterator(); it.hasNext(); remove(it.next())) ;
        }
    }

    /**
     * Load the condition identified by {@code id} from the data storage and cache it.
     *
     * @param id the condition unique identifier
     * @return The built condition
     * @throws LoadingException when the condition is not found or an exception has occurred while building the result.
     */
    @Override
    public Condition load(final long id) throws LoadingException {
        return CACHE.getAndPut(id, this::fetch);
    }

    @Override
    public void evict(final long id) {
        final Condition bool = CACHE.remove(id);
        if (bool instanceof Condition.Tree) {
            remove(((Condition.Tree) bool).getTree());
        } else {
            ConditionFactory.evict(id);
        }
    }

    @Override
    public void evict() {
        CACHE.clearAll();
        ConditionFactory.evict();
    }

    @SuppressWarnings("unchecked")
    private Condition fetch(final long id) {
        final Decider btn = finder.find(id);
        if (btn == null) {
            throw new LoadingException("decider", id);
        }
        if (btn instanceof PlainDecider) {
            final PlainDecider plain = (PlainDecider) btn;
            return Condition.of(plain.getTemplate().getBaseClass(), plain.getId(), plain.getScript());
        }
        final MixedDecider root = (MixedDecider) btn;
        if (root.getNodes().isEmpty()) {
            return Decision.empty();
        }
        final BTree tree = BTree.of(root.getLabel(), root.getOperator(), root.isNegated());
        final Iterator<ChildNode> childIterator = ((Set<ChildNode>) root.getNodes()).iterator();
        final Map<Long, Node> nodeMap = new HashMap<>(root.getNodes().size());
        nodeMap.put(id, tree);
        while (childIterator.hasNext()) {
            final ChildNode child = childIterator.next();
            final Node node = parser.parse(child);
            // The ORDER BY guarantees that the parents are created before their children
            ((Operand) nodeMap.get(child.getParent().getId())).add(node);
            nodeMap.put(child.getId(), node);
        }
        return Condition.of(tree);
    }
}
