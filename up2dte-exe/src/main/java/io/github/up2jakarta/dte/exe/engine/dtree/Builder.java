package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.DTree;
import io.github.up2jakarta.dte.exe.engine.Decision;

/**
 * Root DTree Builder implementation.
 */
@SuppressWarnings("unchecked")
public abstract class Builder implements IBaseBuilder<NodeBuilder<Builder.When>, Builder.Then> {

    private final DTree tree;

    /**
     * Public constructor.
     *
     * @param name the decision tree name
     */
    protected Builder(final String name) {
        this.tree = DTree.of(name);
    }

    @Override
    public NodeBuilder<When> when(final Decision decision) {
        final When aux = new When(decision);
        return new NodeBuilder<>(aux, decision);
    }

    @Override
    public Then then(final Calculation rule) {
        return new Then(Rule.of(rule));
    }

    @Override
    public DTree build() {
        return tree;
    }


    /**
     * Root Then Builder implementation.
     */
    public final class When implements IElseBuilder<NodeBuilder<When>, ThenBuilder<When>, NodeBuilder<IBuilder>> {

        /**
         * Private constructor.
         *
         * @param child the child node
         */
        private When(final Decision child) {
            tree.add(child);
        }

        @Override
        public NodeBuilder<When> when(final Decision decision) {
            return new NodeBuilder(this, tree.add(decision));
        }

        @Override
        public ThenBuilder<When> then(final Calculation rule) {
            return new ThenBuilder(this, tree.last(), Rule.of(rule));
        }

        @Override
        public NodeBuilder<IBuilder> otherwise() {
            return new NodeBuilder<>(this, tree.add(Decision.empty()));
        }

        @Override
        public DTree build() {
            return Builder.this.build();
        }

    }

    /**
     * Root Then Builder implementation.
     */
    public final class Then implements IBaseBuilder<WhenBuilder<Then>, Then> {

        /**
         * Private constructor.
         *
         * @param child the child node
         */
        private Then(final Rule child) {
            tree.add(child);
        }

        @Override
        public WhenBuilder<Then> when(final Decision decision) {
            return new WhenBuilder<>(this, tree.first(), decision);
        }

        @Override
        public Then then(final Calculation rule) {
            tree.add(Rule.of(rule));
            return this;
        }

        @Override
        public DTree build() {
            return Builder.this.build();
        }
    }


}
