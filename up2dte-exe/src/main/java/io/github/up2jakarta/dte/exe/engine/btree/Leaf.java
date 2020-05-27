package io.github.up2jakarta.dte.exe.engine.btree;

import io.github.up2jakarta.dte.exe.engine.Condition;

import java.util.Map;

/**
 * Engine complex leaf condition node representation.
 *
 * @author A.ABBESSI
 */
public interface Leaf extends Node, Condition {

    /**
     * Factory method for {@code Leaf} instances.
     *
     * @param negated   Is the condition should be negated
     * @param condition the condition
     * @return an instance of {@code Leaf}
     */
    static Leaf of(Condition condition, boolean negated) {
        if (condition instanceof Leaf) {
            final Leaf node = (Leaf) condition;
            if (negated) {
                node.negate();
            }
            return node;
        }
        return new LeafImpl(condition, negated);
    }

    /**
     * Factory method for {@code Leaf} instances.
     *
     * @param condition the condition
     * @return an instance of {@code Leaf} with the flag {@link #isNegated()} equals to {@code false}
     */
    static Leaf of(Condition condition) {
        return of(condition, false);
    }

    /**
     * Return teh associated condition.
     *
     * @return the related condition.
     */
    Condition getCondition();

    /**
     * Default implementation of Operand.
     */
    final class LeafImpl extends Node.Base implements Leaf {

        private final Condition condition;

        /**
         * Private constructor.
         *
         * @param condition the related condition
         * @param negated   should inverse the condition
         */
        LeafImpl(final Condition condition, final boolean negated) {
            super(negated);
            this.condition = condition;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return condition.isFulfilled(context) ^ isNegated();
        }

        @Override
        public Condition getCondition() {
            return condition;
        }

        @Override
        public String toString() {
            return super.toString() + ':' + condition;
        }
    }
}
