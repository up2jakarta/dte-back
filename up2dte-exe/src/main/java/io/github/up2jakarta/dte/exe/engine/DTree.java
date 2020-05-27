package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Objects;

/**
 * Calculation complex tree representation.
 *
 * @param <C> the children node type
 * @author A.ABBESSI
 */
public interface DTree<C extends Node<?>> extends Node<C> {

    /**
     * Factory method for {@code DTree} instances.
     *
     * @param name the tree name
     * @param <C>  the children node type
     * @return an instance of Tree
     */
    static <C extends Node<?>> DTree<C> of(final String name) {
        Objects.requireNonNull(name, "The name is required");
        return new TreeImpl<>(name);
    }

    /**
     * Cleans the decision nodes that does not have children.
     * And cuts the nodes that have only default decisions to its sub-children.
     *
     * @param node the tree node
     * @return {@code true} if node should be cleaned
     */
    @SuppressWarnings("unchecked")
    static boolean clean(final Node node) {
        final ListIterator<Node> listIterator = node.iterator();
        if (!listIterator.hasNext()) {
            return !(node instanceof Rule);
        }
        // Clean Rules
        final Node first = listIterator.next();
        if (first instanceof Rule) {
            clean(first);
            while (listIterator.hasNext()) {
                for (final Iterator<Node> it = listIterator.next().iterator(); it.hasNext(); it.remove()) {
                    it.next();
                }
            }
            return false;
        }
        listIterator.previous();
        // Clean Decisions
        while (listIterator.hasNext()) {
            if (clean(listIterator.next())) {
                listIterator.remove();
            }
        }
        // Cut default sub-children to node
        while (node.size() == 1 && node.first().isDefault()) {
            final Node def = node.first();
            listIterator.remove();
            for (final Iterator<Node> it = def.iterator(); it.hasNext(); node.add(it.next())) ;
        }
        return node.size() == 0;
    }

    /**
     * Default implementation of Tree.
     *
     * @param <C> the children node type
     */
    final class TreeImpl<C extends Node<?>> extends BaseImpl<C> implements DTree<C> {

        private final String name;

        /**
         * Private constructor.
         *
         * @param name the tree name
         */
        private TreeImpl(final String name) {
            this.name = name;
        }

        @Override
        public String toString() {
            return name;
        }
    }

    /**
     * DTree Builder implementation.
     */
    final class Builder extends io.github.up2jakarta.dte.exe.engine.dtree.Builder {

        /**
         * Public constructor for Builder.
         *
         * @param name the decision tree name
         */
        public Builder(final String name) {
            super(name);
        }

        @Override
        public DTree build() {
            final DTree tree = super.build();
            clean(tree);
            return tree;
        }
    }

}
