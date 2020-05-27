package io.github.up2jakarta.dte.exe.script;

import org.junit.Test;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.BTree;
import io.github.up2jakarta.dte.exe.engine.btree.Leaf;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;

import static org.junit.Assert.*;
import static io.github.up2jakarta.dte.exe.engine.TestHelper.condition;

public class BTreeTests {

    @Test
    public void shouldSameLeaf() {
        // Given
        final Leaf leaf = Leaf.of(condition("a"));

        // When
        final Leaf leaf2 = Leaf.of(leaf);

        // Then
        assertSame(leaf, leaf2);
        assertEquals(leaf2.toString(), leaf.toString());
    }

    @Test
    public void shouldSameLeafAndNegated() {
        // Given
        final Leaf leaf = Leaf.of(condition("a"));

        // When
        final Leaf leaf2 = Leaf.of(leaf, true);

        // Then
        assertSame(leaf, leaf2);
        assertTrue(leaf.isNegated());
    }

    @Test
    public void testSize() {
        // Given
        final Leaf leaf = Leaf.of(condition("a"));
        final Operand operand = Operand.of(Operator.XOR);

        // When
        operand.add(leaf);

        // Then
        assertEquals(1, operand.size());
    }

    @Test
    public void shouldCleanEmptyOperand() {
        // Given
        final Operand operand1 = Operand.of(Operator.XOR);
        final Operand operand11 = Operand.of(Operator.AND);
        final Operand operand12 = Operand.of(Operator.XOR);
        final Operand operand2 = Operand.of(Operator.OR);
        final BTree tree = BTree.of("Test", Operator.OR);


        // When
        operand1.add(operand11);
        operand1.add(operand12);
        tree.add(operand1);
        tree.add(operand2);
        BTree.clean(tree);

        // Then
        assertEquals(0, tree.size());
    }

    @Test
    public void shouldKeepOperand() {
        // Given
        final Leaf leaf1 = Leaf.of(condition("a"));
        final Leaf leaf2 = Leaf.of(condition("a"));
        final Leaf leaf = Leaf.of(condition("a"));
        final Operand operand = Operand.of(Operator.XOR);
        final BTree tree = BTree.of("Test", Operator.OR);


        // When
        operand.add(leaf1);
        operand.add(leaf2);
        tree.add(operand);
        tree.add(leaf);
        BTree.clean(tree);

        // Then
        assertEquals(2, tree.size());
        assertEquals(operand, tree.childAt(0));
        assertEquals(leaf, tree.childAt(1));
        assertEquals(2, operand.size());
    }

    @Test
    public void shouldCleanUnnecessaryOperand() {
        // Given
        final Leaf leaf = Leaf.of(condition("a"));
        final Operand operand = Operand.of(Operator.XOR);
        final BTree tree = BTree.of("Test", Operator.OR);


        // When
        operand.add(leaf);
        tree.add(operand);
        BTree.clean(tree);

        // Then
        assertEquals(1, tree.size());
        assertEquals(leaf, tree.childAt(0));
    }

    @Test
    public void shouldCleanUnnecessaryNegatedOperand() {
        // Given
        final Leaf leaf = Leaf.of(condition("a"), false);
        final Operand operand = Operand.of(Operator.XOR, true);
        final BTree tree = BTree.of("Test", Operator.OR);


        // When
        operand.add(leaf);
        tree.add(operand);
        BTree.clean(tree);

        // Then
        assertEquals(1, tree.size());
        assertEquals(leaf, tree.childAt(0));
        assertTrue(leaf.isNegated());
    }
}
