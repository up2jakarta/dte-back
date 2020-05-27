package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.btree.Node;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;

import java.util.ListIterator;
import java.util.Map;

/**
 * Engine complex condition tree representation.
 *
 * @author A.ABBESSI
 */
public interface BTree extends Operand, Condition {

    /**
     * factory method for {@code BTree} instances.
     *
     * @param name     the expression name
     * @param operator the children operator
     * @param negated  should negate the operands result
     * @return an instance of {@code BTree} with the flag {@link #isNegated()} equals to {@code false}
     */
    static BTree of(String name, Operator operator, boolean negated) {
        return new BTreeImpl(name, operator, negated);
    }

    /**
     * factory method for {@code BTree} instances.
     *
     * @param name     the expression name
     * @param operator the children operator
     * @return an instance of {@code BTree} with the flag {@link #isNegated()} equals to {@code false}
     */
    static BTree of(String name, Operator operator) {
        return new BTreeImpl(name, operator, false);
    }

    /**
     * Clean the operand nodes that does not have children.
     * And cut the operands that have only one node to its sub-children..
     *
     * @param parent the operand node
     * @return {@code true} if node should be cleaned
     */
    static boolean clean(final Operand parent) {
        final ListIterator<Node> it = parent.iterator();
        while (it.hasNext()) {
            final Node node = it.next();
            if (node instanceof Operand) {
                final Operand operand = (Operand) node;
                if (clean(operand)) {
                    it.remove();
                } else if (operand.size() == 1) {
                    final Node child = operand.childAt(0);
                    if (operand.isNegated()) {
                        child.negate();
                    }
                    it.set(child);
                }
            }
        }
        return parent.size() == 0;
    }

    /**
     * Default implementation of BTree.
     */
    final class BTreeImpl extends Operand.OperandImpl implements BTree {

        private final String name;

        /**
         * Private constructor.
         *
         * @param name     the tree name
         * @param operator the operator
         * @param negated  should negate the operands result
         */
        private BTreeImpl(final String name, final Operator operator, final boolean negated) {
            super(operator, negated);
            this.name = name;
        }

        @Override
        public boolean isFulfilled(final Map<String, Object> context) {
            return BTreeNavigator.isFulfilled(this, context);
        }

        @Override
        public String toString() {
            return isNegated() ? "! " + name : name;
        }
    }

    /**
     * BTree Builder implementation with overloaded constructors.
     */
    class Builder extends io.github.up2jakarta.dte.exe.engine.btree.Builder<Builder> {

        /**
         * Constructor for Builder from a condition.
         *
         * @param condition the related condition
         */
        public Builder(final Condition condition) {
            super(condition);
        }

        /**
         * Constructor for Builder from a script based condition.
         *
         * @param id     the script identifier
         * @param script the script source code
         */
        public Builder(final long id, final String script) {
            super(id, script);
        }

        /**
         * Constructor for Builder from a lazy condition.
         *
         * @param engine   the DTE engine
         * @param calleeId the lazy condition identifier
         */
        public Builder(final StaticEngine engine, final long calleeId) {
            super(engine, calleeId);
        }
    }

}
