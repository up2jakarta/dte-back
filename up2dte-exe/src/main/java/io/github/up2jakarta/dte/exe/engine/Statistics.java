package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.loader.ComputerLoader;
import io.github.up2jakarta.dte.exe.loader.DeciderLoader;

/**
 * Engine statistics model which encapsulate cache regions size and loaded scripts.
 */
@SuppressWarnings("unused")
public final class Statistics {

    public static final Statistics INSTANCE = new Statistics();

    private Statistics() {
    }

    /**
     * Get the total count of cached computers in the cache region.
     *
     * @return the size of the cache
     */
    public long getCachedComputerCount() {
        return ComputerLoader.getCacheRegionSize();
    }

    /**
     * Get the total count of cached deciders in the cache region.
     *
     * @return the size of the cache
     */
    public long getCachedDeciderCount() {
        return DeciderLoader.getCacheRegionSize();
    }

    /**
     * Get the total count of cached conditions in the cache region.
     *
     * @return the size of the cache
     */
    public long getCachedConditionCount() {
        return ConditionFactory.getCacheRegionSize();
    }

    /**
     * Get the total number of compiled conditions in the cache region.
     *
     * @return the number of compiled conditions
     */
    public long getCompiledConditionCount() {
        return ConditionFactory.getLoadedScriptCount();
    }

    /**
     * Get the total count of cached decisions in the cache region.
     *
     * @return the size of the cache
     */
    public long getCachedDecisionCount() {
        return DecisionFactory.getCacheRegionSize();
    }

    /**
     * Get the total number of compiled decisions in the cache region.
     *
     * @return the number of compiled decisions
     */
    public long getCompiledDecisionCount() {
        return DecisionFactory.getLoadedScriptCount();
    }

    /**
     * Get the total count of cached calculations in the cache region.
     *
     * @return the size of the calculation cache
     */
    public long getCachedCalculationCount() {
        return CalculationFactory.getCacheRegionSize();
    }

    /**
     * Get the total number of compiled calculations in the cache region.
     *
     * @return the number of compiled calculations
     */
    public long getCompiledCalculationCount() {
        return CalculationFactory.getLoadedScriptCount();
    }
}
