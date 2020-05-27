package io.github.up2jakarta.dte.exe.engine.dtree;

import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.Decision;

import java.util.Objects;

/**
 * Base decision tree builder.
 *
 * @param <W> the type of the when builder
 * @param <T> the type of the then builder
 */
public interface IBaseBuilder<W extends IBaseBuilder, T extends IBaseBuilder> extends IBuilder {

    /**
     * <p>Creates a rule node with the given {@code rule}.
     *
     * @param rule the calculation
     * @return the builder
     */
    T then(Calculation rule);

    /**
     * Executes the automatic task by invoking the calculation identified by the parameter {@code calleeId}.
     *
     * @param engine   the DTE engine
     * @param calleeId the identifier of the callee calculation.
     * @return the builder
     */
    default T then(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return then(Calculation.of(engine, calleeId));
    }

    /**
     * Executes the automatic task by evaluating the parameter {@code script}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    default T then(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return then(Calculation.of(BaseScript.class, id, script));
    }

    /**
     * Creates a decision node with the given {@code decision}.
     *
     * @param decision the decision node
     * @return the builder
     */
    W when(Decision decision);


    /**
     * Creates a decision node with the given {@code condition}.
     *
     * @param condition the condition of the decision
     * @param negated   the condition should be negated
     * @return the builder
     */
    default W when(Condition condition, boolean negated) {
        Objects.requireNonNull(condition, "The condition is required");
        return when(Decision.of(condition, negated));
    }

    /**
     * <p>Creates a decision node with the condition identified by {@code calleeId} and the flag {@code negated}.
     * This created decision is fulfilled if and only one of the following rules is true:</p>
     * <ul>
     * <li>The condition is verified and the parameter {@code negated} is {@code false}</li>
     * <li>The condition is not verified and the parameter {@code negated} is {@code true}</li>
     * </ul>
     *
     * @param engine   the DTE engine
     * @param negated  the condition should be negated
     * @param calleeId the callee condition identifier
     * @return the builder
     */
    default W when(final StaticEngine engine, final long calleeId, boolean negated) {
        Objects.requireNonNull(engine, "The engine is required");
        return when(Condition.of(engine, calleeId), negated);
    }

    /**
     * <p>Creates a decision node with the condition evaluated by {@code script} and the flag {@code negated}.
     * This created decision is fulfilled if and only one of the following rules is true:</p>
     * <ul>
     * <li>The condition is verified and the parameter {@code negated} is {@code false}</li>
     * <li>The condition is not verified and the parameter {@code negated} is {@code true}</li>
     * </ul>
     *
     * @param id      the script identifier
     * @param script  the script source code
     * @param negated the condition should be negated
     * @return the builder
     */
    default W when(final long id, final String script, boolean negated) {
        Objects.requireNonNull(script, "The script is required");
        return when(Condition.of(BaseScript.class, id, script), negated);
    }

    /**
     * <p>Creates a temporary decision node with the condition evaluated by {@code script}.</p>
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    default W when(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return when(Decision.of(BaseScript.class, id, script));
    }

}
