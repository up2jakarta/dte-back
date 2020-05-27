package io.github.up2jakarta.dte.jpa.api;

import org.junit.Assert;
import org.junit.Test;
import io.github.up2jakarta.dte.jpa.TestUtil;
import io.github.up2jakarta.dte.jpa.api.dtree.IChildNode;
import io.github.up2jakarta.dte.jpa.api.dtree.IParentNode;
import io.github.up2jakarta.dte.jpa.entities.DTreeComputer;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreeChildNode;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreeDeciderNode;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreeDefaultNode;
import io.github.up2jakarta.dte.jpa.entities.dtn.DTreeMixedComputer;

public class DTreeTests {

    private final Group group = TestUtil.DTN_SPACE;

    @Test
    public void shouldAdd() {
        // Given
        final IParentNode parent = new DTreeMixedComputer(group);
        final IChildNode child = new DTreeDefaultNode();

        // When
        parent.addChild(child, 1);

        // Then
        Assert.assertEquals(1, parent.getChildren().size());
    }

    @Test
    public void shouldRemove() {
        // Given
        final IChildNode child = new DTreeDefaultNode();
        final IParentNode parent = new DTreeMixedComputer(group);
        {
            parent.addChild(child, 1);
        }

        // When
        parent.removeChild(child);

        // Then
        Assert.assertEquals(0, parent.getChildren().size());
    }

    @Test
    public void shouldIgnoreNullWhenAdd() {
        // Given
        final IParentNode parent = new DTreeMixedComputer(group);

        // When
        parent.addChild(null, 1);

        // Then
        Assert.assertEquals(0, parent.getChildren().size());
    }

    @Test
    public void shouldIgnoreNullWhenRemove() {
        // Given
        final IParentNode parent = new DTreeMixedComputer(group);

        // When
        parent.removeChild(null);

        // Then
        Assert.assertEquals(0, parent.getChildren().size());
    }

    @Test
    public void shouldDTreeComputerToString() {
        // Given
        final DTreeComputer calc = new DTreeMixedComputer(group);
        {
            calc.setLabel("Test");
        }

        // Then
        Assert.assertEquals(String.valueOf(calc.getGroup()) + '/' + calc.getLabel(), calc.toString());
    }

    @Test
    public void shouldNotAddWhenSameOrder() {
        // When
        final DTreeChildNode child1 = new DTreeDefaultNode();
        final DTreeChildNode child2 = new DTreeDeciderNode();
        final DTreeMixedComputer calc = new DTreeMixedComputer(group);
        {
            calc.setLabel("Test");
            calc.addChild(child1, 1);
            calc.addChild(child2, 1);
        }

        // Then
        Assert.assertEquals(1, calc.getChildren().size());
    }

    @Test
    public void shouldNotAddWhenSameChild() {
        // When
        final DTreeChildNode child = new DTreeDefaultNode();
        final DTreeMixedComputer calc = new DTreeMixedComputer(group);
        {
            calc.setLabel("Test");
            calc.addChild(child, 1);
            calc.addChild(child, 2);
        }

        // Then
        Assert.assertEquals(1, calc.getChildren().size());
    }

    @Test
    public void shouldAddWhenNotSameOrder() {
        // When
        final DTreeChildNode child1 = new DTreeDefaultNode();
        final DTreeChildNode child2 = new DTreeDeciderNode();
        final DTreeMixedComputer calc = new DTreeMixedComputer(group);
        {
            calc.setLabel("Test");
            calc.addChild(child1, 1);
            calc.addChild(child2, 2);
        }

        // Then
        Assert.assertEquals(2, calc.getChildren().size());
    }

    @Test
    public void shouldAddNode() {
        // Given
        final DTreeMixedComputer root = new DTreeMixedComputer(group);
        {
            root.setLabel("M");
        }
        final DTreeChildNode child = new DTreeDeciderNode();

        // When
        root.addChild(child, 1);

        // Then
        Assert.assertEquals(1, root.getChildren().size());
        Assert.assertEquals(1, root.getNodes().size());
        Assert.assertSame(root, child.getParent());
        Assert.assertEquals(Integer.valueOf(1), child.getOrder());
        Assert.assertEquals("DTE/DTN/M/1", child.toString());
    }

    @Test
    public void shouldNotEquals() {
        // Given
        final DTreeMixedComputer root = new DTreeMixedComputer(group);
        {
            root.setLabel("M");
        }
        final DTreeChildNode child1 = new DTreeDefaultNode();
        final DTreeChildNode child2 = new DTreeDeciderNode();

        // When Then
        Assert.assertNotEquals(child1, new Object());
        Assert.assertEquals(child1, child2);

        // When Then
        root.addChild(child1, 0);
        Assert.assertNotEquals(child1, child2);
        Assert.assertNotEquals(child1, child2);

        // When Then
        root.addChild(child1, 1);
        Assert.assertNotEquals(child1, child2);

        // When Then
        root.addChild(child2, 2);

        // When Then
        root.addChild(child1, 2);
        root.addChild(child2, 3);
        Assert.assertNotEquals(child1, child2);
        Assert.assertEquals(2, root.getChildren().size());
        Assert.assertEquals(2, root.getNodes().size());
    }

    @Test
    public void shouldEqualsWhenSameOrder() {
        // Given
        final DTreeMixedComputer root = new DTreeMixedComputer(group);
        {
            root.setLabel("M");
        }
        final DTreeChildNode child1 = new DTreeDefaultNode();
        final DTreeChildNode child2 = new DTreeDeciderNode();

        // When
        root.addChild(child1, 0);
        root.addChild(child2, 0);

        // Then
        Assert.assertEquals(child1, child1);
        Assert.assertEquals(1, root.getChildren().size());
        Assert.assertEquals(1, root.getNodes().size());
    }

    @Test
    public void shouldNotEqualsWhenDistinctParent() {
        // Given
        final DTreeMixedComputer root = new DTreeMixedComputer(group);
        {
            root.setLabel("M");
        }
        final DTreeChildNode child1 = new DTreeDefaultNode();
        final DTreeChildNode child2 = new DTreeDefaultNode();

        // When
        root.addChild(child1, 0);
        child1.addChild(child2, 0);

        // Then
        Assert.assertEquals(1, root.getChildren().size());
        Assert.assertEquals(1, child1.getChildren().size());
        Assert.assertNotEquals(child1, child2);
    }

}
