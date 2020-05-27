package io.github.up2jakarta.dte.exe.engine;

import groovy.lang.GroovyClassLoader;
import org.codehaus.groovy.control.CompilerConfiguration;
import org.codehaus.groovy.control.MultipleCompilationErrorsException;
import org.codehaus.groovy.control.customizers.CompilationCustomizer;
import org.codehaus.groovy.control.customizers.ImportCustomizer;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.exe.script.StaticScript;

import java.time.DayOfWeek;
import java.time.LocalTime;

/**
 * ClassLoader which can load scripts classes. The loaded scripts are cached.
 */
final class ScriptLoader extends GroovyClassLoader {

    private static final ImportCustomizer IMPORTS = new ImportCustomizer();

    static {
        IMPORTS.addStarImports(LocalTime.class.getPackage().getName());
        IMPORTS.addStaticStars(DayOfWeek.class.getName());
    }

    /**
     * Protected constructor for ScriptClassLoader.
     *
     * @param config      the compiler config
     * @param customizers the compiler customizers
     */
    ScriptLoader(final CompilerConfiguration config, final CompilationCustomizer... customizers) {
        super(ScriptLoader.class.getClassLoader(), config);
        config.addCompilationCustomizers(IMPORTS);
        config.addCompilationCustomizers(customizers);
    }

    /**
     * Removes script from the class loader cache.
     *
     * @param scriptName of the compiled script name.
     */
    void evict(final String scriptName) {
        super.removeClassCacheEntry(scriptName);
    }

    /**
     * Creates a Script instance from the given {@code script} code source.
     *
     * @param script the script to compile and load
     * @param key    the script unique key
     * @return the class compiled from the script
     * @throws CompilationException if there is a syntax errors
     */
    @SuppressWarnings({"unchecked"})
    public StaticScript parse(final String key, final String script) throws CompilationException {
        try {
            // No need to synchronize: GroovyClassLoader provides synchronization around parse
            return StaticScript.of(super.parseClass(script, key));
        } catch (MultipleCompilationErrorsException cee) {
            throw new CompilationException(cee);
        }
    }
}
