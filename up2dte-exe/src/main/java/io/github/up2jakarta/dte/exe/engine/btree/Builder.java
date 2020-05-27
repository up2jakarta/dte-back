package io.github.up2jakarta.dte.exe.engine.btree;

import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.BTree;
import io.github.up2jakarta.dte.exe.engine.Condition;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;

/**
 * BTree operand builder.
 *
 * @param <T> the type of the builder
 */
@SuppressWarnings("unchecked")
public abstract class Builder<T extends Builder> {

    private final List<Node> operands = new ArrayList<>();
    private Operator operator = null;

    /**
     * Constructor for Builder from a condition.
     *
     * @param condition the related condition
     */
    public Builder(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        operands.add(Leaf.of(condition));
    }

    /**
     * Constructor for Builder from a script based condition.
     *
     * @param id     the script identifier
     * @param script the script source code
     */
    public Builder(final long id, final String script) {
        Objects.requireNonNull(script, "The loader is script");
        operands.add(Leaf.of(Condition.of(BaseScript.class, id, script)));
    }

    /**
     * Constructor for Builder from a lazy condition.
     *
     * @param engine   the DTE engine
     * @param calleeId the lazy condition identifier
     */
    public Builder(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        operands.add(Leaf.of(Condition.of(engine, calleeId)));
    }

    // Lazy joining

    /**
     * Creates a right operand from the lazy condition identified by the given {@code calleeId} within negation.
     * Then join the left operand to the new created right operand with the {@link Operator#AND}.
     *
     * @param engine   the DTE engine
     * @param calleeId the name of the node.
     * @return the builder
     */
    public T and(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return join(Operator.AND, Leaf.of(Condition.of(engine, calleeId)));
    }

    /**
     * Creates a right operand from the lazy condition identified by the given {@code calleeId} within negation.
     * Then join the left operand to the new created right operand with the {@link Operator#AND}.
     *
     * @param engine   the DTE engine
     * @param calleeId the lazy condition identifier
     * @return the builder
     */
    public T andNot(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return join(Operator.AND, Leaf.of(Condition.of(engine, calleeId), true));
    }

    /**
     * Creates a right operand from the lazy condition identified by the given {@code calleeId} within negation.
     * Then join the left operand to the new created right operand with the {@link Operator#OR}.
     *
     * @param engine   the DTE engine
     * @param calleeId the lazy condition identifier
     * @return the builder
     */
    public T or(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return join(Operator.OR, Leaf.of(Condition.of(engine, calleeId)));
    }

    /**
     * Creates a right operand from the lazy condition identified by the given {@code calleeId} within negation.
     * Then join the left operand to the new created right operand with the {@link Operator#OR}.
     *
     * @param engine   the DTE engine
     * @param calleeId the lazy condition identifier
     * @return the builder
     */
    public T orNot(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return join(Operator.OR, Leaf.of(Condition.of(engine, calleeId), true));
    }

    /**
     * Creates a right operand from the lazy condition identified by the given {@code calleeId} within negation.
     * Then join the left operand to the new created right operand with the {@link Operator#XOR}.
     *
     * @param engine   the DTE engine
     * @param calleeId the lazy condition identifier
     * @return the builder
     */
    public T xor(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return join(Operator.XOR, Leaf.of(Condition.of(engine, calleeId)));
    }

    /**
     * Creates a right operand from the lazy condition identified by the given {@code calleeId} within negation.
     * Then join the left operand to the new created right operand with the {@link Operator#XOR}.
     *
     * @param engine   the DTE engine
     * @param calleeId the lazy condition identifier
     * @return the builder
     */
    public T xorNot(final StaticEngine engine, final long calleeId) {
        Objects.requireNonNull(engine, "The engine is required");
        return join(Operator.XOR, Leaf.of(Condition.of(engine, calleeId), true));
    }

    // Script joining

    /**
     * Creates a right operand from the given {@code script} source code without negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#AND}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    public T and(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return join(Operator.AND, Leaf.of(Condition.of(BaseScript.class, id, script)));
    }

    /**
     * Creates a right operand from the given {@code script} source code within negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#AND}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    public T andNot(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return join(Operator.AND, Leaf.of(Condition.of(BaseScript.class, id, script), true));
    }

    /**
     * Creates a right operand from the given {@code script} source code without negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#OR}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    public T or(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return join(Operator.OR, Leaf.of(Condition.of(BaseScript.class, id, script)));
    }

    /**
     * Creates a right operand from the given {@code script} source code within negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#OR}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    public T orNot(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return join(Operator.OR, Leaf.of(Condition.of(BaseScript.class, id, script), true));
    }

    /**
     * Creates a right operand from the given {@code script} source code without negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#XOR}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    public T xor(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return join(Operator.XOR, Leaf.of(Condition.of(BaseScript.class, id, script)));
    }

    /**
     * Creates a right operand from the given {@code script} source code within negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#XOR}.
     *
     * @param id     the script identifier
     * @param script the script source code
     * @return the builder
     */
    public T xorNot(final long id, final String script) {
        Objects.requireNonNull(script, "The script is required");
        return join(Operator.XOR, Leaf.of(Condition.of(BaseScript.class, id, script), true));
    }

    // Conditions joining

    /**
     * Creates a right operand from the given {@code condition} without negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#AND}.
     *
     * @param condition the related condition
     * @return the builder
     */
    public T and(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        return join(Operator.AND, Leaf.of(condition));
    }

    /**
     * Creates a right operand from the given {@code condition} within negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#AND}.
     *
     * @param condition the related condition
     * @return the builder
     */
    public T andNot(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        return join(Operator.AND, Leaf.of(condition, true));
    }

    /**
     * Creates a right operand from the given {@code condition} without negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#OR}.
     *
     * @param condition the related condition
     * @return the builder
     */
    public T or(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        return join(Operator.OR, Leaf.of(condition));
    }

    /**
     * Creates a right operand from the given {@code condition} within negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#OR}.
     *
     * @param condition the related condition
     * @return the builder
     */
    public T orNot(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        return join(Operator.OR, Leaf.of(condition, true));
    }

    /**
     * Creates a right operand from the given {@code condition} without negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#XOR}.
     *
     * @param condition the related condition
     * @return the builder
     */
    public T xor(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        return join(Operator.XOR, Leaf.of(condition));
    }

    /**
     * Creates a right operand from the given {@code condition} within negating its evaluation.
     * Then join the left operand to the new created right operand with the {@link Operator#XOR}.
     *
     * @param condition the related condition
     * @return the builder
     */
    public T xorNot(final Condition condition) {
        Objects.requireNonNull(condition, "The condition is required");
        return join(Operator.XOR, Leaf.of(condition, true));
    }

    // Builders joining

    /**
     * Join the left operand to the new right operand {@code builder} with the {@link Operator#OR}.
     *
     * @param builder the BTree builder
     * @return the builder
     */
    public final T or(final Builder<T> builder) {
        Objects.requireNonNull(builder, "The builder is required");
        return join(Operator.OR, builder, false);
    }

    /**
     * Join the left operand to the new right operand {@code builder} with the {@link Operator#OR} with negation.
     *
     * @param builder the BTree builder
     * @return the builder
     */
    public final T orNot(final Builder<T> builder) {
        Objects.requireNonNull(builder, "The builder is required");
        return join(Operator.OR, builder, true);
    }

    /**
     * Join the left operand to the new right operand {@code builder} with the {@link Operator#AND}.
     *
     * @param builder the BTree builder
     * @return the builder
     */
    public final T and(final Builder<T> builder) {
        Objects.requireNonNull(builder, "The builder is required");
        return join(Operator.AND, builder, false);
    }

    /**
     * Join the left operand to the new right operand {@code builder} with the {@link Operator#AND} with negation.
     *
     * @param builder the BTree builder
     * @return the builder
     */
    public final T andNot(final Builder<T> builder) {
        Objects.requireNonNull(builder, "The builder is required");
        return join(Operator.AND, builder, true);
    }

    /**
     * Join the left operand to the new right operand {@code builder} with the {@link Operator#XOR}.
     *
     * @param builder the BTree builder
     * @return the builder
     */
    public final T xor(final Builder<T> builder) {
        Objects.requireNonNull(builder, "The builder is required");
        return join(Operator.XOR, builder, false);
    }

    /**
     * Join the left operand to the new right operand {@code builder} with the {@link Operator#XOR} with negation.
     *
     * @param builder the BTree builder
     * @return the builder
     */
    public final T xorNot(final Builder<T> builder) {
        Objects.requireNonNull(builder, "The builder is required");
        return join(Operator.XOR, builder, true);
    }

    /**
     * Negate the current operand.
     *
     * @return the builder
     */
    public final T not() {
        if (operands.size() == 1) {
            operands.get(0).negate();
        } else {
            final Operand node = Operand.of(this.operator, true);
            for (final Node operand : operands) {
                node.add(operand);
            }
            operands.clear();
            operands.add(node);
            this.operator = null;
        }
        return (T) this;
    }

    /**
     * Build the boolean tree and return it.
     *
     * @param name the tree name
     * @return the built tree
     */
    public final BTree build(final String name) {
        if (operands.size() == 1 && operands.get(0) instanceof Operand) {
            final Operand root = (Operand) operands.get(0);
            final BTree tree = BTree.of(name, root.getOperator(), root.isNegated());
            for (final Iterator<Node> it = root.iterator(); it.hasNext(); tree.add(it.next())) ;
            return tree;
        }
        final BTree tree = BTree.of(name, (operator != null) ? operator : Operator.OR);
        for (final Node operand : operands) {
            tree.add(operand);
        }
        return tree;
    }

    /**
     * Join the left operand to the new right operand {@code builder} with the given {@code operator}.
     *
     * @param operator the operator
     * @param builder  the BTree builder
     * @param negated  should negate the result of the builder
     * @return the builder
     */
    private T join(final Operator operator, final Builder<T> builder, final boolean negated) {
        if (builder.operands.size() == 1) {
            final Node first = builder.operands.get(0);
            if (negated) {
                first.negate();
            }
            return join(operator, first);
        }
        final Operand operand = Operand.of(builder.operator, negated);
        for (final Node node : builder.operands) {
            operand.add(node);
        }
        return join(operator, operand);
    }

    /**
     * Join the left operand to the new right operand {@code node} with the given {@code operator}.
     *
     * @param operator the operator
     * @param node     the right operand
     * @return the builder
     */
    private T join(final Operator operator, final Node node) {
        if (this.operator == null || this.operator == operator) {
            operands.add(node);
        } else {
            final Operand old = Operand.of(this.operator, false);
            for (final Node operand : operands) {
                old.add(operand);
            }
            operands.clear();
            operands.add(old);
            operands.add(node);

        }
        this.operator = operator;
        return (T) this;
    }

}
