package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.script.ExecutionException;

import java.util.Map;

/**
 * Engine execution condition entry.
 *
 * @author A.ABBESSI
 */
public interface Condition {

    /**
     * Factory method for creating a new instance of {@code Condition}.
     *
     * @param base   the script base class
     * @param id     the identifier
     * @param script the script source code
     * @return an instance of {@code Condition}
     */
    static Condition of(final Class<? extends groovy.lang.Script> base, final long id, final String script) {
        return ConditionFactory.of(base, id, script);
    }

    /**
     * Factory method for creating a new lazy instance of {@code Condition}.
     *
     * @param engine   the DTE engine
     * @param calleeId the callee condition identifier
     * @return an instance of {@code Condition}
     */
    static Condition of(final StaticEngine engine, final long calleeId) {
        return new LinkImpl(engine, calleeId);
    }

    /**
     * Factory method for creating a new instance of {@code Condition}.
     *
     * @param tree the condition tree
     * @return an instance of Calculation
     */
    static Condition of(final BTree tree) {
        BTree.clean(tree);
        return new Condition.TreeImpl(tree);
    }

    /**
     * Test if the condition is fulfilled depending on the given input {@code context}.
     *
     * @param context the execution context
     * @return {@code true} if the condition is evaluated to {@code true}, otherwise {@code false}
     * @throws ExecutionException if a execution error has been occurred at runtime
     */
    boolean isFulfilled(Map<String, Object> context) throws ExecutionException;

    /**
     * Tree based calculation.
     */
    interface Tree extends Condition {

        /**
         * @return the binary tree.
         */
        BTree getTree();
    }

    /**
     * Lazy condition implementation.
     */
    final class LinkImpl implements Condition {

        private final long calleeId;
        private final StaticEngine engine;

        /**
         * Private constructor.
         *
         * @param engine   the DTE engine
         * @param calleeId the callee tree identifier
         */
        private LinkImpl(final StaticEngine engine, final long calleeId) {
            this.engine = engine;
            this.calleeId = calleeId;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return engine.isFulfilled(calleeId, context);
        }

        @Override
        public String toString() {
            return String.valueOf(calleeId);
        }
    }

    /**
     * Complex condition tree rule implementation.
     */
    final class TreeImpl implements Tree {

        private final BTree tree;

        /**
         * Private constructor.
         *
         * @param model the tree model
         */
        private TreeImpl(final BTree model) {
            this.tree = model;
        }


        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return tree.isFulfilled(context);
        }


        @Override
        public BTree getTree() {
            return tree;
        }

        @Override
        public String toString() {
            return tree.toString();
        }
    }

}
