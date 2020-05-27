package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;
import io.github.up2jakarta.dte.exe.script.ScriptContext;

import java.util.Iterator;
import java.util.ListIterator;
import java.util.Map;

/**
 * Decision Tree Navigator.
 *
 * @author A.ABBESSI
 */
final class DTreeNavigator {

    /**
     * Private constructor.
     */
    private DTreeNavigator() {
    }

    /**
     * Navigate the tree to return all fulfilled results.
     *
     * @param tree    the calculation tree
     * @param context the execution context
     * @param <C>     the Children type
     * @return the context output after the execution
     */
    static <C extends Node<?>> Map<String, Object> resolve(final DTree<C> tree, final Map<String, Object> context) {
        final ScriptContext ctx = new ScriptContext(context);
        visit(tree, ctx);
        return ctx.getOut();
    }


    /**
     * Visit decision tree node.
     *
     * @param node    the node
     * @param context the execution context
     */
    @SuppressWarnings("unchecked")
    private static void visit(final Node<?> node, final ScriptContext context) {
        if (node.size() != 0) {
            final Node<?> first = node.first();
            if (first instanceof Rule) {
                visitRules(((Node<Rule>) node).iterator(), context);
            } else {
                visitDecisions(((Node<Decision<?>>) node).iterator(), context);
            }
        }
    }

    /**
     * Visit decision's nodes.
     *
     * @param it  the decisions iterator
     * @param ctx the execution context
     */
    private static void visitDecisions(final Iterator<Decision<?>> it, final ScriptContext ctx) {
        while (it.hasNext()) {
            final Decision<?> decision = it.next();
            final boolean ok = decision.isFulfilled(ctx);
            if (ok) {
                visit(decision, ctx);
                break;
            }
        }
    }

    /**
     * Visit rule's nodes.
     *
     * @param it  the rules iterator
     * @param ctx the execution context
     */
    private static void visitRules(final ListIterator<Rule> it, final ScriptContext ctx) {
        final Rule first = it.next();
        it.previous();
        do {
            final Rule rule = it.next();
            final Map<String, Object> output = rule.resolve(ctx);
            ctx.putAll(output);
        } while (it.hasNext());
        visitDecisions(first.iterator(), ctx);
    }
}
