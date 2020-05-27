package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.Decision;
import io.github.up2jakarta.dte.exe.loader.ParsingException;

import java.lang.reflect.Proxy;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;

import static java.lang.reflect.Proxy.newProxyInstance;

/**
 * Complex calculation node representation.
 *
 * @param <C> the children node type
 * @author A.ABBESSI
 */
public interface Node<C extends Node<?>> {

    /**
     * @return decisions over the children in proper order
     */
    ListIterator<C> iterator();

    /**
     * @return the number of children
     */
    int size();

    /**
     * Returns the last child node.
     *
     * @return the element at the last position
     * @throws IndexOutOfBoundsException if the children is empty
     */
    default C last() {
        return childAt(size() - 1);
    }

    /**
     * Returns the first child node.
     *
     * @return the element at the first position
     * @throws IndexOutOfBoundsException if the children is empty
     */
    default C first() {
        return childAt(0);
    }

    /**
     * Returns the child node at the specified position.
     * Note that if the child is a proxy then it will be unproxied.
     *
     * @param index index of the element to return
     * @return the element at the specified position
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    C childAt(int index);

    /**
     * Add the given {@code node} to the list of children.
     *
     * @param node the new child node
     * @return the managed node
     */
    C add(C node);

    /**
     * Tests if the node is a default decision.
     *
     * @return {@code true} if default decision, otherwise {@code false}
     */
    default boolean isDefault() {
        return false;
    }

    /**
     * Base implementation of Operand.
     *
     * @param <C> the children node type
     */
    abstract class BaseImpl<C extends Node<?>> implements Node<C> {

        private static final Class[] PROXY_INTERFACES = new Class[]{Rule.class};
        private static final ClassLoader PROXY_LOADER = Rule.class.getClassLoader();

        private final List<C> children = new ArrayList<>();

        /**
         * Private constructor.
         */
        protected BaseImpl() {
        }

        @Override
        @SuppressWarnings("unchecked")
        public C childAt(final int index) {
            final C child = children.get(index);
            if (index == 0 || isNotProxy(child)) {
                return child;
            }
            return ((LockHandler<C>) Proxy.getInvocationHandler(child)).getNode();
        }

        @Override
        public int size() {
            return children.size();
        }

        @Override
        public ListIterator<C> iterator() {
            return children.listIterator();
        }

        @Override
        @SuppressWarnings("unchecked")
        public final C add(final C c) {
            if (this == c) {
                throw new ParsingException("cannot add a child to itself");
            } else if (children.isEmpty() && c.isDefault()) {
                throw new ParsingException("default decision cannot be the first");
            } else if (this instanceof Rule && c instanceof Rule) {
                throw new ParsingException("cannot add a rule node to another rule node");
            } else if (!children.isEmpty()) {
                final C last = last();
                if (last.isDefault()) {
                    throw new ParsingException("default decision is set before");
                } else if (last instanceof Rule && !(c instanceof Rule)) {
                    throw new ParsingException("cannot add a decision node beside calculations");
                } else if (last instanceof Decision && !(c instanceof Decision)) {
                    throw new ParsingException("cannot add a calculation node beside decisions");
                } else if (c.size() != 0) {
                    throw new ParsingException("only first rule is allowed to have children");
                } else if (c instanceof Rule && isNotProxy(c)) {
                    final Rule proxy = (Rule) newProxyInstance(PROXY_LOADER, PROXY_INTERFACES, new LockHandler(c));
                    children.add((C) proxy);
                    return (C) proxy;
                }
            }
            children.add(c);
            return c;
        }

        /**
         * Test if the specified is not a DTE proxy.
         *
         * @param node the child node
         * @return {@code true} if not proxy, otherwise {@code false}
         */
        private boolean isNotProxy(final C node) {
            if (Proxy.isProxyClass(node.getClass())) {
                return !(Proxy.getInvocationHandler(node) instanceof LockHandler);
            }
            return true;
        }
    }
}
