package io.github.up2jakarta.dte.exe.engine;

import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.btree.Leaf;
import io.github.up2jakarta.dte.exe.engine.btree.Node;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;
import io.github.up2jakarta.dte.exe.script.ScriptContext;

import java.util.Iterator;
import java.util.Map;

/**
 * Binary Tree Navigator.
 *
 * @author A.ABBESSI
 */
final class BTreeNavigator {

    /**
     * Private constructor.
     */
    private BTreeNavigator() {
    }

    /**
     * Navigate the condition tree to return the evaluated result.
     *
     * @param tree    the binary tree
     * @param context the execution context
     * @return the execution result
     */
    static boolean isFulfilled(final BTree tree, final Map<String, Object> context) {
        return visit(tree, new ScriptContext(context));
    }

    /**
     * Navigate the sub-tree to return the evaluated condition.
     *
     * @param node    the intermediate node
     * @param context the execution context
     * @return the boolean result
     */
    private static boolean visit(final Node node, final ScriptContext context) {
        if (node instanceof Leaf) {
            return ((Leaf) node).isFulfilled(context);
        }
        final Operand operand = (Operand) node;
        final Iterator<Node> it = operand.iterator();
        if (!it.hasNext()) {
            return true;
        }
        if (operand.getOperator() == Operator.XOR) {
            boolean b = visit(it.next(), context);
            while (it.hasNext()) {
                b = b ^ visit(it.next(), context);
            }
            return b ^ node.isNegated();
        }
        while (it.hasNext()) {
            final boolean b = visit(it.next(), context);
            if ((b && operand.getOperator() == Operator.OR) || (!b && operand.getOperator() == Operator.AND)) {
                return b ^ node.isNegated();
            }
        }
        return (operand.getOperator() == Operator.AND) ^ node.isNegated();
    }

}
