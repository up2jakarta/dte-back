package io.github.up2jakarta.dte.exe.engine.btree;

import io.github.up2jakarta.dte.exe.api.Operator;

import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

/**
 * BTree operand node representation.
 *
 * @author A.ABBESSI
 */
public interface Operand extends Node {

    /**
     * Factory method for {@code Operand} instances.
     *
     * @param negated  should negate the operands result
     * @param operator the operator
     * @return an instance of Operand as a parent
     */
    static Operand of(Operator operator, boolean negated) {
        return new OperandImpl(operator, negated);
    }

    /**
     * Factory method for {@code Operand} instances.
     *
     * @param operator the operator
     * @return an instance of {@code Operand} with the flag {@link #isNegated()} equals to {@code false}
     */
    static Operand of(Operator operator) {
        return new OperandImpl(operator, false);
    }

    /**
     * @return the children operator, as a parent
     */
    Operator getOperator();

    /**
     * @return operands over the children in proper order
     */
    ListIterator<Node> iterator();

    /**
     * Add the given {@code node} to the list of operands.
     *
     * @param node the new child node
     */
    void add(Node node);

    /**
     * @return the number of children
     */
    int size();

    /**
     * Returns the child node at the specified position.
     *
     * @param index index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range of operands
     */
    Node childAt(int index);

    /**
     * Default implementation of Operand.
     */
    class OperandImpl extends Node.Base implements Operand {

        private final Operator operator;
        private final List<Node> operands = new ArrayList<>();

        /**
         * Private constructor.
         *
         * @param operator the operator
         * @param negated  should negate the operands result
         */
        protected OperandImpl(final Operator operator, final boolean negated) {
            super(negated);
            this.operator = operator;
        }

        @Override
        public final Operator getOperator() {
            return operator;
        }

        @Override
        public final ListIterator<Node> iterator() {
            return operands.listIterator();
        }

        @Override
        public final void add(final Node node) {
            operands.add(node);
            node.setParent(this, operands.size());
        }

        @Override
        public int size() {
            return operands.size();
        }

        @Override
        public Node childAt(final int index) {
            return operands.get(index);
        }

    }
}
