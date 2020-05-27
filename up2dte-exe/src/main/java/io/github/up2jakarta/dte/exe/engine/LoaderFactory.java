package io.github.up2jakarta.dte.exe.engine;

import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.runtime.memoize.EvictableCache;
import org.codehaus.groovy.runtime.memoize.UnlimitedConcurrentCache;

/**
 * DTE Script Parser light implementation, i.e without static type checking.
 *
 * @author A.ABBESSI
 */
final class LoaderFactory {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection ")
    private final EvictableCache<String, ScriptLoader> cache = new UnlimitedConcurrentCache<>();

    /**
     * Protected constructor for ScriptCompiler.
     */
    LoaderFactory() {
    }

    /**
     * Factory method for {@code ScriptLoader} instances from the specified script base class.
     * This method will always cache instances.
     *
     * @param base the script base class
     * @return a single instance of {@code ScriptLoader}
     */
    ScriptLoader of(final Class<? extends groovy.lang.Script> base) {
        return cache.getAndPut(base.getName(), (n) -> {
            final CompilerConfiguration config = new CompilerConfiguration();
            config.setScriptBaseClass(n);
            return new ScriptLoader(config);
        });
    }

    /**
     * Removes all compiled scripts from the cache.
     */
    void evict() {
        for (final ScriptLoader loader : cache.values()) {
            loader.clearCache();
        }
    }

    /**
     * Removes the compiled script identified by the given {@code key}  from the cache.
     *
     * @param key the script unique key
     */
    void evict(final String key) {
        for (final ScriptLoader loader : cache.values()) {
            loader.evict(key);
        }
    }

    /**
     * Count and return the total number of loaded scripts from distinct loaders.
     *
     * @return the number of loaded scripts
     */
    long count() {
        long total = 0;
        for (final ScriptLoader loader : cache.values()) {
            total += loader.getLoadedClasses().length;
        }
        return total;
    }

}

