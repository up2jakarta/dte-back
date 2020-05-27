package io.github.up2jakarta.dte.exe.engine.dtree;

/**
 * When node builder.
 *
 * @param <W> the type of the when builder
 * @param <T> the type of the then builder
 * @param <E> the type of the else builder
 * @param <P> the type of the parent builder
 */
interface IWhenBuilder<W extends IBaseBuilder, T extends IBaseBuilder, E extends IBaseBuilder, P extends IBuilder>
        extends INodeBuilder<W, T, P>, IElseBuilder<W, T, E> {
}
