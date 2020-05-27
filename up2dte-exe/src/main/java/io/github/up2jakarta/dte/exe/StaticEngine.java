package io.github.up2jakarta.dte.exe;

import groovy.lang.Script;
import io.github.up2jakarta.dte.dsl.BusinessException;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.ScriptCompiler;
import io.github.up2jakarta.dte.exe.engine.Statistics;
import io.github.up2jakarta.dte.exe.loader.ComputerLoader;
import io.github.up2jakarta.dte.exe.loader.DeciderLoader;
import io.github.up2jakarta.dte.exe.loader.Finder;
import io.github.up2jakarta.dte.exe.loader.Loader;
import io.github.up2jakarta.dte.exe.script.CompilationException;
import io.github.up2jakarta.dte.exe.script.ExecutionException;

import java.util.Map;

/**
 * Static Engine entry point for generic operations on decision trees.
 *
 * @author A.ABBESSI
 * @see StaticEngine#compile(Class, String, Map)
 * @see StaticEngine#validate(Class, String, Map)
 * @see StaticEngine#version()
 */
public class StaticEngine {

    private final Loader<Calculation> computers;
    private final Loader<Condition> deciders;

    /**
     * Public constructor for StaticEngine.
     *
     * @param deciders  the decider loader
     * @param computers the computer loader
     */
    public StaticEngine(final Loader<Calculation> computers, final Loader<Condition> deciders) {
        this.computers = computers;
        this.deciders = deciders;
    }

    /**
     * Public constructor for StaticEngine.
     *
     * @param deciders  the decider finder
     * @param computers the computer finder
     */
    public StaticEngine(final Finder<Computer> computers, final Finder<Decider> deciders) {
        this.computers = new ComputerLoader(computers, this);
        this.deciders = new DeciderLoader(deciders, this);
    }

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     * Extract the output variables typing and return them.
     *
     * @param base   the script base class
     * @param script the script source code
     * @param typing the input typing
     * @return the output typing
     * @throws CompilationException if the script does not compile
     */
    public static Map<String, Class<?>> compile(final Class<? extends Script> base, final String script,
                                                final Map<String, Class<?>> typing) throws CompilationException {
        return ScriptCompiler.compile(base, script, typing);
    }

    /**
     * Check the syntax and types of the given {@code script} within DTE extensions.
     *
     * @param base   the script base class
     * @param script the script source code
     * @param typing the variables(inputs and outputs) typing
     * @throws CompilationException if the script does not compile
     */
    public static void validate(final Class<? extends Script> base, final String script,
                                final Map<String, Class<?>> typing) throws CompilationException {
        ScriptCompiler.validate(base, script, typing);
    }

    /**
     * @return the engine statistics.
     */
    public static Statistics statistics() {
        return Statistics.INSTANCE;
    }

    /**
     * @return the engine version.
     */
    public static Version version() {
        return Version.INSTANCE;
    }

    /**
     * Executes the computer without modified the input {@code context}.
     *
     * @param id      the computer identifier
     * @param context the computer input context
     * @return The output context
     * @throws BusinessException  if the computer throw a business exception
     * @throws ExecutionException if a execution error has been occurred at runtime
     */
    public final Map<String, Object> resolve(final long id, final Map<String, Object> context)
            throws ExecutionException, BusinessException {
        // TODO validate inputs and outputs
        return computers.load(id).resolve(context);
    }

    /**
     * Test if the condition is fulfilled depending on the given input {@code context}.
     *
     * @param id      the computer identifier
     * @param context the execution context
     * @return {@code true} if the condition is evaluated to {@code true}, otherwise {@code false}
     * @throws ExecutionException if a execution error has been occurred at runtime
     */
    public final boolean isFulfilled(final long id, final Map<String, Object> context) throws ExecutionException {
        // TODO validate inputs and outputs
        return deciders.load(id).isFulfilled(context);
    }
}
