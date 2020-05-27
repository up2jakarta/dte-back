package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.engine.dtree.Node;

import java.util.Map;

/**
 * Complex calculation node representation.
 *
 * @param <C> the children node type
 * @author A.ABBESSI
 */
public interface Decision<C extends Node<?>> extends Node<C>, Condition {

    /**
     * Factory method for creating a new instance of empty {@code Decision}.
     *
     * @param <C> the children node type
     * @return an instance of {@code Decision}
     */
    static <C extends Node<?>> Decision<C> empty() {
        return new EmptyImpl<>();
    }

    /**
     * Factory method for creating a new instance of temporary {@code Decision}.
     *
     * @param base   the script base class
     * @param id     the decision identifier
     * @param script the script source code
     * @param <C>    the children node type
     * @return an instance of {@code Decision}
     */
    static <C extends Node<?>> Decision<C> of(final Class<? extends groovy.lang.Script> base,
                                              final long id, final String script) {
        return new ScriptImpl<>(base, id, script);
    }

    /**
     * Factory method for {@code Decision} instances.
     *
     * @param negated   Is the condition should be negated
     * @param condition the condition
     * @param <C>       the children node type
     * @return an instance of {@code Decision}
     */
    @SuppressWarnings("unchecked")
    static <C extends Node<?>> Decision<C> of(final Condition condition, final boolean negated) {
        if (condition instanceof Decision && !negated) {
            return (Decision<C>) condition;
        }
        if (condition instanceof DecisionImpl) {
            final DecisionImpl<C> result = (DecisionImpl<C>) condition;
            result.negated = true;
            return result;
        }
        return new DecisionImpl<>(condition, negated);
    }

    /**
     * Return teh associated condition.
     * <p>
     * If the decision is default {@link #isDefault()} return {@code null}.
     *
     * @return the related condition.
     */
    Condition getCondition();

    /**
     * Default implementation of decision node.
     *
     * @param <C> the children node type
     */
    class DecisionImpl<C extends Node<?>> extends BaseImpl<C> implements Decision<C> {

        private final Condition condition;
        private boolean negated;

        /**
         * Private constructor.
         *
         * @param c the entry condition
         * @param n should inverse condition
         */
        private DecisionImpl(final Condition c, final boolean n) {
            this.condition = c;
            this.negated = n;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return condition.isFulfilled(context) ^ negated;
        }

        @Override
        public Condition getCondition() {
            return condition;
        }

        @Override
        public String toString() {
            return (negated ? "!" : "") + condition;
        }
    }

    /**
     * Temporary decision node implementation.
     *
     * @param <C> the children node type
     */
    class ScriptImpl<C extends Node<?>> extends BaseImpl<C> implements Decision<C> {

        private final Condition condition;

        /**
         * Private constructor for ScriptImpl.
         *
         * @param base   the script base class
         * @param id     the decision identifier
         * @param script the script source code
         */
        private ScriptImpl(final Class<? extends groovy.lang.Script> base, final long id, final String script) {
            this.condition = DecisionFactory.of(base, id, script);
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return condition.isFulfilled(context);
        }

        @Override
        public Condition getCondition() {
            return condition;
        }

        @Override
        public String toString() {
            return String.valueOf(condition);
        }
    }

    /**
     * Empty decision represents else one, it always is fulfilled.
     *
     * @param <C> the children node type
     */
    class EmptyImpl<C extends Node<?>> extends BaseImpl<C> implements Decision<C> {

        /**
         * Private constructor.
         */
        private EmptyImpl() {
        }

        @Override
        public boolean isDefault() {
            return true;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return true;
        }

        @Override
        public Condition getCondition() {
            return null;
        }

        @Override
        public String toString() {
            return "Default";
        }
    }
}
