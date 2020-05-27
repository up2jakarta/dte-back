package io.github.up2jakarta.dte.exe.engine.btree;

/**
 * BTree node representation.
 *
 * @author A.ABBESSI
 */
public interface Node {

    /**
     * @return {@code true} if the engine should negate the result, as a child
     */
    boolean isNegated();

    /**
     * Inverses the {@code negated flag}.
     */
    void negate();

    /**
     * As a child, set the parent node and the order, useful for {@link #toString()}.
     *
     * @param parent the parent node
     * @param order  the child order
     */
    void setParent(Operand parent, Integer order);

    @Override
    String toString();

    /**
     * Abstract implementation of Node.
     */
    abstract class Base implements Node {

        private Operand parent;
        private Integer order;
        private boolean negated;

        /**
         * Private constructor.
         *
         * @param negated should inverse the condition
         */
        Base(final boolean negated) {
            this.negated = negated;
        }

        @Override
        public final boolean isNegated() {
            return negated;
        }

        @Override
        public final void negate() {
            negated = !negated;
        }

        @Override
        public final void setParent(final Operand parent, final Integer order) {
            this.parent = parent;
            this.order = order;
        }

        @Override
        public String toString() {
            if (order == null) {
                return "";
            }
            final String separator = isNegated() ? "/!" : "/";
            return parent + separator + order;
        }
    }
}
