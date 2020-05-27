package io.github.up2jakarta.dte.exe.engine;

import groovy.lang.Script;
import org.codehaus.groovy.runtime.memoize.EvictableCache;
import org.codehaus.groovy.runtime.memoize.UnlimitedConcurrentCache;

import java.util.Map;

/**
 * Decision Factory implementation.
 */
public final class DecisionFactory {

    private static final EvictableCache<Long, CacheEntry> CACHE = new UnlimitedConcurrentCache<>();
    private static final LoaderFactory FACTORY = new LoaderFactory();

    private DecisionFactory() {
    }

    /**
     * Get the total count of cached decisions in this region.
     *
     * @return the size of the cache
     */
    static long getCacheRegionSize() {
        return CACHE.size();
    }

    /**
     * Count and return the total number of compiled scripts from loader factory.
     *
     * @return the number of compiled decisions
     */
    static long getLoadedScriptCount() {
        return FACTORY.count();
    }

    /**
     * Evict all decision scripts from the cache.
     */
    public static void evict() {
        FACTORY.evict();
        CACHE.clearAll();
    }

    /**
     * Removes the decision identified by {@code id} from the cache.
     *
     * @param uid a boolean condition key
     */
    public static void evict(final long uid) {
        final DynamicScript entry = CACHE.remove(uid);
        if (entry != null) {
            FACTORY.evict(entry.getScripName());
        }
    }

    /**
     * Factory method for creating a cached {@link Condition}.
     *
     * @param base   the script base class
     * @param uid    the script unique identifier
     * @param source the script source code
     * @return an instance of {@code Condition}
     */
    static Condition of(final Class<? extends Script> base, final long uid, final String source) {
        CacheEntry decision = CACHE.get(uid);
        if (null == decision) {
            decision = new CacheEntry(base, uid, source);
            if (null != CACHE.put(uid, decision)) {
                throw new RuntimeException("Should synchronise decision cache");
            }
        } else {
            decision.update(base, source);
        }
        return decision;
    }

    /**
     * Decision script cache entry implementation.
     */
    private static final class CacheEntry extends DynamicScript implements Condition {
        private CacheEntry(final Class<? extends Script> base, final long uid, final String source) {
            super(base, uid, source);
        }

        @Override
        ScriptLoader getParser(final Class<? extends Script> base) {
            return FACTORY.of(base);
        }

        @Override
        protected char getPrefix() {
            return TEMP_DIGIT;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return ConditionEvaluator.isFulfilled(this, context);
        }
    }
}
