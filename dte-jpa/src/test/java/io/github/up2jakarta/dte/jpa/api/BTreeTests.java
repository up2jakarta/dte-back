package io.github.up2jakarta.dte.jpa.api;

import io.github.up2jakarta.dte.jpa.entities.btn.*;
import org.junit.Assert;
import org.junit.Test;
import io.github.up2jakarta.dte.jpa.TestUtil;
import io.github.up2jakarta.dte.jpa.api.btree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.btree.IOperatorNode;
import io.github.up2jakarta.dte.jpa.entities.BTreeDecider;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.btn.*;

public class BTreeTests {

    private final Group group = TestUtil.BTN_SPACE;

    @Test
    public void shouldAdd() {
        // Given
        final IOperatorNode parent = new BTreeMixedDecider(group);
        final IChildNode child = new BTreeOperatorNode();

        // When
        parent.addOperand(child, 1);

        // Then
        Assert.assertEquals(1, parent.getOperands().size());
    }

    @Test
    public void shouldRemove() {
        // Given
        final IChildNode child = new BTreeOperatorNode();
        final IOperatorNode parent = new BTreeMixedDecider(group);
        {
            parent.addOperand(child, 1);
        }

        // When
        parent.removeOperand(child);

        // Then
        Assert.assertEquals(0, parent.getOperands().size());
    }

    @Test
    public void shouldIgnoreNullWhenAdd() {
        // Given
        final IOperatorNode parent = new BTreeMixedDecider(group);

        // When
        parent.addOperand(null, 1);

        // Then
        Assert.assertEquals(0, parent.getOperands().size());
    }

    @Test
    public void shouldIgnoreNullWhenRemove() {
        // Given
        final IOperatorNode parent = new BTreeMixedDecider(group);

        // When
        parent.removeOperand(null);

        // Then
        Assert.assertEquals(0, parent.getOperands().size());
    }

    @Test
    public void shouldBTreeDeciderToString() {
        // Given
        final BTreeDecider calc = new BTreeMixedDecider(group);
        {
            calc.setLabel("Test");
        }

        // Then
        Assert.assertEquals(String.valueOf(calc.getGroup()) + '/' + calc.getLabel(), calc.toString());
    }

    @Test
    public void shouldNotAddWhenSameOrder() {
        // When
        final BTreeChildNode child1 = new BTreeDeciderNode();
        final BTreeChildNode child2 = new BTreeLocalNode(group);
        final IOperatorNode calc = new BTreeMixedDecider(group);
        {
            calc.setLabel("Test");
            calc.addOperand(child1, 1);
            calc.addOperand(child2, 1);
        }

        // Then
        Assert.assertEquals(1, calc.getOperands().size());
    }

    @Test
    public void shouldNotAddWhenSameChild() {
        // When
        final BTreePlainDecider bool = new BTreePlainDecider(group);
        {
            bool.setScript("a == b");
        }
        final BTreeDeciderNode child = new BTreeDeciderNode();
        {
            child.setDecider(bool);
        }
        final IOperatorNode calc = new BTreeMixedDecider(group);
        {
            calc.setLabel("Test");
            calc.addOperand(child, 1);
            calc.addOperand(child, 2);
        }

        // Then
        Assert.assertEquals(1, calc.getOperands().size());
        Assert.assertEquals(child, calc.getOperands().iterator().next());
    }

    @Test
    public void shouldAddWhenNotSameOrder() {
        // When
        final BTreeChildNode child1 = new BTreeDeciderNode();
        final BTreeLocalNode child2 = new BTreeLocalNode(group);
        final IOperatorNode calc = new BTreeMixedDecider(group);
        {
            calc.setLabel("Root");
            calc.addOperand(child1, 1);
            calc.addOperand(child2, 2);
        }

        // Then
        Assert.assertEquals(2, calc.getOperands().size());
        Assert.assertEquals(child2, child2.getDecider());
        Assert.assertEquals(calc, child2.getParent());
        Assert.assertEquals(calc, child2.getRoot());
        Assert.assertEquals("DTE/BTN/Root/2", child2.toString());
    }

    @Test
    public void shouldNotEquals() {
        // Given
        final BTreeMixedDecider root = new BTreeMixedDecider(group);
        {
            root.setLabel("M");
        }
        final BTreeChildNode child1 = new BTreeDeciderNode();
        final BTreeChildNode child2 = new BTreeLocalNode(group);

        // When Then
        Assert.assertNotEquals(child1, new Object());
        Assert.assertEquals(child1, child2);

        // When Then
        root.addOperand(child1, 0);
        Assert.assertNotEquals(child1, child2);
        Assert.assertNotEquals(child1, child2);

        // When Then
        root.addOperand(child1, 1);
        Assert.assertNotEquals(child1, child2);

        // When Then
        root.addOperand(child2, 2);

        // When Then
        root.addOperand(child1, 2);
        root.addOperand(child2, 3);
        Assert.assertNotEquals(child1, child2);
        Assert.assertEquals(2, root.getOperands().size());
        Assert.assertEquals(2, root.getNodes().size());
    }

    @Test
    public void shouldEqualsWhenSameOrder() {
        // Given
        final BTreeMixedDecider root = new BTreeMixedDecider(group);
        {
            root.setLabel("M");
        }
        final BTreeChildNode child1 = new BTreeDeciderNode();
        final BTreeChildNode child2 = new BTreeLocalNode(group);

        // When
        root.addOperand(child1, 0);
        root.addOperand(child2, 0);

        // Then
        Assert.assertEquals(child1, child1);
        Assert.assertEquals(1, root.getOperands().size());
        Assert.assertEquals(1, root.getNodes().size());
    }

    @Test
    public void shouldNotEqualsWhenDistinctParent() {
        // Given
        final BTreeMixedDecider root = new BTreeMixedDecider(group);
        {
            root.setLabel("M");
        }
        final BTreeOperatorNode child1 = new BTreeOperatorNode();
        final BTreeChildNode child2 = new BTreeDeciderNode();

        // When
        root.addOperand(child1, 0);
        child1.addOperand(child2, 0);

        // Then
        Assert.assertEquals(1, root.getOperands().size());
        Assert.assertEquals(1, child1.getOperands().size());
        Assert.assertNotEquals(child1, child2);
    }
}
