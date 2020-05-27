package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.dsl.BusinessException;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.script.ExecutionException;

import java.util.Map;

/**
 * Engine execution calculation entry.
 *
 * @author A.ABBESSI
 */
public interface Calculation {

    /**
     * Factory method for creating a new instance of {@code Calculation}.
     *
     * @param tree the calculation tree
     * @return an instance of {@code Calculation}
     */
    static Calculation of(final DTree<?> tree) {
        DTree.clean(tree);
        return new TreeImpl(tree);
    }

    /**
     * Factory method for creating a new instance of {@code Calculation}.
     *
     * @param base   the script base class
     * @param id     the identifier
     * @param script the script source code
     * @return an instance of {@code Calculation}
     */
    static Calculation of(final Class<? extends groovy.lang.Script> base, final long id, final String script) {
        return CalculationFactory.of(base, id, script);
    }

    /**
     * Factory method for creating a new lazy instance of {@code Calculation}.
     *
     * @param engine   the DTE engine
     * @param calleeId the callee calculation identifier
     * @return an instance of {@code Calculation}
     */
    static Calculation of(final StaticEngine engine, final long calleeId) {
        return new LinkImpl(engine, calleeId);
    }

    /**
     * Evaluates the calculation with the given input {@code context}.
     *
     * @param context the input
     * @return the result output
     * @throws BusinessException  if the script throw a business exception
     * @throws ExecutionException if a execution error has been occurred at runtime
     */
    Map<String, Object> resolve(Map<String, Object> context) throws ExecutionException, BusinessException;

    /**
     * Tree based calculation.
     */
    interface Tree extends Calculation {

        /**
         * @return the decision tree.
         */
        DTree getTree();
    }

    /**
     * Complex calculation tree implementation.
     */
    final class TreeImpl implements Tree {

        private final DTree<?> tree;

        /**
         * Private constructor for TreeImpl.
         *
         * @param model the tree model
         */
        private TreeImpl(final DTree<?> model) {
            this.tree = model;
        }

        @Override
        public Map<String, Object> resolve(final Map<String, Object> context) {
            return DTreeNavigator.resolve(tree, context);
        }

        @Override
        public DTree getTree() {
            return tree;
        }

        @Override
        public String toString() {
            return tree.toString();
        }
    }

    /**
     * Lazy calculation implementation.
     */
    final class LinkImpl implements Calculation {

        private final long calleeId;
        private final StaticEngine engine;

        /**
         * Private constructor for LinkImpl.
         *
         * @param engine   the DTE engine
         * @param calleeId the callee tree identifier
         */
        private LinkImpl(final StaticEngine engine, final long calleeId) {
            this.engine = engine;
            this.calleeId = calleeId;
        }

        @Override
        public Map<String, Object> resolve(final Map<String, Object> context) {
            return engine.resolve(calleeId, context);
        }

        @Override
        public String toString() {
            return String.valueOf(calleeId);
        }
    }
}
