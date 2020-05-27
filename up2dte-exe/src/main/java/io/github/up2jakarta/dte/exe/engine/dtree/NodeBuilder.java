package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.DTree;
import io.github.up2jakarta.dte.exe.engine.Decision;

/**
 * Intermediate Operand Builder Implementation.
 *
 * @param <P> the type of the parent builder
 */

public class NodeBuilder<P extends IBuilder> implements INodeBuilder<NodeBuilder<WhenBuilder<P>>, ThenBuilder<P>, P> {

    private final Node node;
    private final P builder;

    /**
     * Private constructor.
     *
     * @param builder the parent builder
     * @param node    the intermediate node
     */
    NodeBuilder(final P builder, final Node node) {
        this.builder = builder;
        this.node = node;
    }

    @Override
    @SuppressWarnings("unchecked")
    public final NodeBuilder<WhenBuilder<P>> when(final Decision decision) {
        final WhenBuilder aux = new WhenBuilder(builder, node, decision);
        return new NodeBuilder<>(aux, decision);
    }

    @Override
    @SuppressWarnings("unchecked")
    public final ThenBuilder<P> then(final Calculation rule) {
        return new ThenBuilder(builder, node, Rule.of(rule));
    }

    @Override
    public final DTree build() {
        return builder.build();
    }

    @Override
    public final P end() {
        return builder;
    }
}
