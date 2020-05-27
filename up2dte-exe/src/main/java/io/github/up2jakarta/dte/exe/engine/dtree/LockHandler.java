package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.loader.ParsingException;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Proxy invocation handler that blocks add child to tree node.
 *
 * @param <T> the node type
 */
class LockHandler<T extends Node<?>> implements InvocationHandler {

    private final T node;

    /**
     * Private constructor.
     *
     * @param node the node to proxy
     */
    LockHandler(final T node) {
        this.node = node;
    }

    @Override
    public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
        if ("add".equals(method.getName())) {
            throw new ParsingException("only first rule is allowed to have children");
        }
        try {
            return method.invoke(node, args);
        } catch (InvocationTargetException ite) {
            throw ite.getCause();
        }
    }

    /**
     * @return the delegate node
     */
    T getNode() {
        return node;
    }
}
