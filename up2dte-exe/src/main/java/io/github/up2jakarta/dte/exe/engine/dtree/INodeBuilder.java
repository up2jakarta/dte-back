package io.github.up2jakarta.dte.exe.engine.dtree;

/**
 * Intermediate node builder.
 *
 * @param <W> the type of the when builder
 * @param <T> the type of the then builder
 * @param <P> the type of the parent builder
 */
interface INodeBuilder<W extends IBaseBuilder, T extends IBaseBuilder, P extends IBuilder>
        extends IBaseBuilder<W, T>, IEndBuilder<P> {
}
