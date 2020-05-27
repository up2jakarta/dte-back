package io.github.up2jakarta.dte.exe.engine;

import groovy.lang.Script;
import org.codehaus.groovy.runtime.memoize.EvictableCache;
import org.codehaus.groovy.runtime.memoize.UnlimitedConcurrentCache;

import java.util.Map;

/**
 * Calculation Factory implementation.
 */
public final class CalculationFactory {

    private static final EvictableCache<Long, CacheEntry> CACHE = new UnlimitedConcurrentCache<>();
    private static final LoaderFactory FACTORY = new LoaderFactory();

    private CalculationFactory() {
    }

    /**
     * Get the total count of cached calculations in this region.
     *
     * @return the size of the cache
     */
    static long getCacheRegionSize() {
        return CACHE.size();
    }

    /**
     * Count and return the total number of compiled scripts from loader factory.
     *
     * @return the number of compiled calculations
     */
    static long getLoadedScriptCount() {
        return FACTORY.count();
    }

    /**
     * Removes the calculation identified by {@code id} from the cache.
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
     * Evict all calculation scripts from the cache.
     */
    public static void evict() {
        FACTORY.evict();
        CACHE.clearAll();
    }

    /**
     * Factory method for creating a cached {@link Calculation}.
     *
     * @param base   the script base class
     * @param id     the script unique identifier
     * @param source the script source code
     * @return an instance of {@link Calculation}
     */
    static Calculation of(final Class<? extends Script> base, final long id, final String source) {
        CacheEntry calculation = CACHE.get(id);
        if (null == calculation) {
            calculation = new CacheEntry(base, id, source);
            if (null != CACHE.put(id, calculation)) {
                throw new RuntimeException("Should synchronise calculation cache");
            }
        } else {
            calculation.update(base, source);
        }
        return calculation;
    }

    /**
     * Calculation script cache entry implementation.
     */
    private static final class CacheEntry extends DynamicScript implements Calculation {
        private CacheEntry(final Class<? extends Script> base, final long id, final String script) {
            super(base, id, script);
        }

        @Override
        ScriptLoader getParser(final Class<? extends Script> base) {
            return FACTORY.of(base);
        }

        @Override
        public Map<String, Object> resolve(final Map<String, Object> context) {
            return CalculationEvaluator.evaluate(this, context);
        }

        @Override
        protected char getPrefix() {
            return CALC_DIGIT;
        }
    }
}
