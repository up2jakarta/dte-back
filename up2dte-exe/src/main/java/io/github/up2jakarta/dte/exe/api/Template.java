package io.github.up2jakarta.dte.exe.api;

import groovy.lang.Script;

/**
 * Script Base Class wrapper, useful for lazy loading.
 */
public interface Template {

    /**
     * Return the base script class, i.e the script which inherit from.
     *
     * @return the base script class
     */
    Class<? extends Script> getBaseClass();
}
