package io.github.up2jakarta.dte.exe.engine.dtree;

/**
 * Else Node builder.
 *
 * @param <W> the type of the when builder
 * @param <T> the type of the then builder
 * @param <E> the type of the else builder
 */
public interface IElseBuilder<W extends IBaseBuilder, T extends IBaseBuilder, E extends IBuilder>
        extends IBaseBuilder<W, T> {

    /**
     * Creates an else decision and moves forward the cursor.
     *
     * @return the builder
     */
    E otherwise();
}
