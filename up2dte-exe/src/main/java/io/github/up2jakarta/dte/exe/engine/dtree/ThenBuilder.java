package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.DTree;
import io.github.up2jakarta.dte.exe.engine.Decision;

/**
 * Then Operand Builder implementation.
 *
 * @param <P> the type of the parent builder
 */
public final class ThenBuilder<P extends IBuilder>
        implements INodeBuilder<WhenBuilder<ThenBuilder<P>>, ThenBuilder<P>, P> {

    private final P builder;
    private final Node<Rule> node;

    /**
     * Private constructor.
     *
     * @param builder the parent builder
     * @param node    the parent node
     * @param child   the child node
     */
    ThenBuilder(final P builder, final Node<Rule> node, final Rule child) {
        this.node = node;
        node.add(child);
        this.builder = builder;

    }

    @Override
    @SuppressWarnings("unchecked")
    public WhenBuilder<ThenBuilder<P>> when(final Decision decision) {
        return new WhenBuilder(this, node.first(), decision);
    }

    @Override
    public ThenBuilder<P> then(final Calculation rule) {
        node.add(Rule.of(rule));
        return this;
    }

    @Override
    public P end() {
        return builder;
    }

    @Override
    public DTree build() {
        return builder.build();
    }
}

