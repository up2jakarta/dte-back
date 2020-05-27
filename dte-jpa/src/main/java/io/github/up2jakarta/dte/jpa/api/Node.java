package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.exe.api.Identifiable;

/**
 * Tree Node interface.
 *
 * @param <T> Tree node concrete type
 */
public interface Node<T extends Identifiable<Long>> {

    /**
     * @return tree root node.
     */
    T getRoot();
}
