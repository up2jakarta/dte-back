package io.github.up2jakarta.dte.exe.engine.dtree;

/**
 * Empty builder with only {@link #end()} operation.
 *
 * @param <P> the type of the parent builder
 */
public interface IEndBuilder<P extends IBuilder> extends IBuilder {

    /**
     * Moves backward the cursor, i.e returns the parent builder.
     *
     * @return the build of parent builder.
     */
    P end();
}
