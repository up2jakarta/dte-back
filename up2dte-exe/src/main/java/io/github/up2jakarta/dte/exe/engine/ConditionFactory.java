package io.github.up2jakarta.dte.exe.engine;

import groovy.lang.Script;
import org.codehaus.groovy.runtime.memoize.EvictableCache;
import org.codehaus.groovy.runtime.memoize.UnlimitedConcurrentCache;

import java.util.Map;

/**
 * Condition Factory implementation.
 */
public final class ConditionFactory {

    private static final EvictableCache<Long, CacheEntry> CACHE = new UnlimitedConcurrentCache<>();
    private static final LoaderFactory FACTORY = new LoaderFactory();

    private ConditionFactory() {
    }

    /**
     * Get the total count of cached conditions in this region.
     *
     * @return the size of the cache
     */
    static long getCacheRegionSize() {
        return CACHE.size();
    }

    /**
     * Count and return the total number of compiled scripts from loader factory.
     *
     * @return the number of compiled conditions
     */
    static long getLoadedScriptCount() {
        return FACTORY.count();
    }

    /**
     * Removes the condition identified by {@code id} from the cache.
     *
     * @param id a boolean condition key
     */
    public static void evict(final long id) {
        final DynamicScript entry = CACHE.remove(id);
        if (entry != null) {
            FACTORY.evict(entry.getScripName());
        }
    }

    /**
     * Evict all condition scripts from the cache.
     */
    public static void evict() {
        FACTORY.evict();
        CACHE.clearAll();
    }

    /**
     * Factory method for creating a cached {@link Condition}.
     *
     * @param base   the script base class
     * @param id     the script unique identifier
     * @param source the script source code
     * @return an instance of {@code Condition}
     */
    static Condition of(final Class<? extends Script> base, final long id, final String source) {
        CacheEntry condition = CACHE.get(id);
        if (null == condition) {
            condition = new CacheEntry(base, id, source);
            if (null != CACHE.put(id, condition)) {
                throw new RuntimeException("Should synchronise condition cache");
            }
        } else {
            condition.update(base, source);
        }
        return condition;
    }

    /**
     * Condition script cache entry implementation.
     */
    private static final class CacheEntry extends DynamicScript implements Condition {
        private CacheEntry(final Class<? extends Script> base, final long id, final String script) {
            super(base, id, script);
        }

        @Override
        ScriptLoader getParser(final Class<? extends Script> base) {
            return FACTORY.of(base);
        }

        @Override
        protected char getPrefix() {
            return BOOL_DIGIT;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return ConditionEvaluator.isFulfilled(this, context);
        }
    }
}
