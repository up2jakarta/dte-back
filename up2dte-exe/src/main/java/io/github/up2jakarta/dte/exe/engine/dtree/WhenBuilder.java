package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.DTree;
import io.github.up2jakarta.dte.exe.engine.Decision;

/**
 * When Operand Builder implementation.
 *
 * @param <P> the type of the parent builder
 */
public final class WhenBuilder<P extends IBuilder> implements
        IWhenBuilder<NodeBuilder<WhenBuilder<P>>, ThenBuilder<WhenBuilder<P>>, NodeBuilder<IEndBuilder<P>>, P> {

    private final P builder;
    private final Node<Decision<?>> node;

    /**
     * Private constructor.
     *
     * @param builder the parent builder
     * @param node    the parent node
     * @param child   the child node
     */
    WhenBuilder(final P builder, final Node<Decision<?>> node, final Decision child) {
        this.node = node;
        node.add(child);
        this.builder = builder;
    }

    @Override
    @SuppressWarnings("unchecked")
    public NodeBuilder<WhenBuilder<P>> when(final Decision decision) {
        return new NodeBuilder(this, node.add(decision));
    }

    @Override
    @SuppressWarnings("unchecked")
    public ThenBuilder<WhenBuilder<P>> then(final Calculation rule) {
        return new ThenBuilder(this, node.last(), Rule.of(rule));
    }

    @Override
    public NodeBuilder<IEndBuilder<P>> otherwise() {
        return new NodeBuilder<>(this, node.add(Decision.empty()));
    }

    @Override
    public DTree build() {
        return builder.build();
    }

    @Override
    public P end() {
        return builder;
    }
}
