package io.github.up2jakarta.dte.exe.loader;

import io.github.up2jakarta.dte.exe.engine.*;
import org.codehaus.groovy.runtime.memoize.EvictableCache;
import org.codehaus.groovy.runtime.memoize.UnlimitedConcurrentCache;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.dtree.ChildNode;
import io.github.up2jakarta.dte.exe.api.dtree.MixedComputer;
import io.github.up2jakarta.dte.exe.api.dtree.PlainComputer;
import io.github.up2jakarta.dte.exe.engine.*;
import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Computer Loader implementation.
 *
 * @author A.ABBESSI
 */
public final class ComputerLoader implements CacheLoader<Calculation> {

    private static final String ERR_FORMAT = "Cannot link the node [%d] to [%d] (%s)";
    private static final EvictableCache<Long, Calculation> CACHE = new UnlimitedConcurrentCache<>();
    private final Finder<Computer> finder;
    private final NodeParser<Node, ChildNode> parser;

    /**
     * Public constructor for ComputerLoader.
     *
     * @param finder the computer finder
     * @param engine the DTE engine
     */
    public ComputerLoader(final Finder<Computer> finder, final StaticEngine engine) {
        this.finder = finder;
        this.parser = new ComputerNodeParser(engine);
    }

    /**
     * Get the total count of cached computers in this region.
     *
     * @return the size of the cache
     */
    public static long getCacheRegionSize() {
        return CACHE.size();
    }

    /**
     * Removes all dependencies of the given {@code node} from the cache.
     *
     * @param node the decision tree node
     */
    @SuppressWarnings("unchecked")
    static void remove(final Node node) {
        if (node instanceof Decision) {
            final Decision d = (Decision) node;
            if (!d.isDefault() && d.getCondition() instanceof CacheEntry) {
                DecisionFactory.evict(((CacheEntry) d.getCondition()).getId());
            }
        }
        if (node instanceof Rule) {
            final Calculation c = ((Rule) node).getCalculation();
            if (c instanceof CacheEntry) {
                CalculationFactory.evict(((CacheEntry) c).getId());
            }
        }
        for (final Iterator<Node> it = node.iterator(); it.hasNext(); remove(it.next())) ;
    }

    /**
     * Loads the calculation identified by {@code id} from the data storage and cache it.
     *
     * @param id the calculation unique identifier
     * @return The built calculation
     * @throws LoadingException when the calculation is not found or an error has occurred while building the result.
     */
    @Override
    public Calculation load(final long id) throws LoadingException {
        return CACHE.getAndPut(id, this::fetch);
    }

    /**
     * Removes the computer identified by {@code id} from the cache.
     *
     * @param id the computer unique identifier
     */
    @Override
    public void evict(final long id) {
        final Calculation calc = CACHE.remove(id);
        if (calc instanceof Calculation.Tree) {
            remove(((Calculation.Tree) calc).getTree());
        } else {
            CalculationFactory.evict(id);
        }
    }

    /**
     * Evict all computers from the cache.
     */
    @Override
    public void evict() {
        CACHE.clearAll();
        CalculationFactory.evict();
        DecisionFactory.evict();
    }

    @SuppressWarnings("unchecked")
    private Calculation fetch(final long id) {
        final Computer dtn = finder.find(id);
        if (dtn == null) {
            throw new LoadingException("computer", id);
        }
        if (dtn instanceof PlainComputer) {
            final PlainComputer plain = (PlainComputer) dtn;
            return Calculation.of(plain.getTemplate().getBaseClass(), plain.getId(), plain.getScript());
        }
        final MixedComputer root = (MixedComputer) dtn;
        final DTree tree = DTree.of(root.getLabel());

        final Iterator<ChildNode> it = ((Set<ChildNode>) root.getNodes()).iterator();
        final Map<Long, Node> nodeMap = new HashMap<>(root.getNodes().size());
        nodeMap.put(id, tree);
        while (it.hasNext()) {
            final ChildNode c = it.next();
            final Node node = parser.parse(c);
            // The ORDER BY guarantees that the parents are created before their children
            try {
                nodeMap.get(c.getParent().getId()).add(node);
            } catch (ParsingException e) {
                final String message = String.format(ERR_FORMAT, c.getId(), c.getParent().getId(), e.getMessage());
                throw new LoadingException("calculation", id, message);
            }
            nodeMap.put(c.getId(), node);
        }
        return Calculation.of(tree);
    }

}
