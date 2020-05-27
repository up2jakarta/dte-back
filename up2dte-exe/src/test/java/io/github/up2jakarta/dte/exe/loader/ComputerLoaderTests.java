package io.github.up2jakarta.dte.exe.loader;

import io.github.up2jakarta.dte.exe.engine.*;
import io.github.up2jakarta.dte.exe.loader.impl.*;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.engine.*;
import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;
import io.github.up2jakarta.dte.exe.loader.impl.*;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class ComputerLoaderTests {

    private static final long TREE_ID = 1000;
    private static final long C_ID = 1001;
    private static final long D_ID = 1002;
    private static final long CALC_ID = 1003;

    private static final Finder<Computer> FINDER = TestHelper.mockFinder(Computer.class);
    private static final ComputerLoader LOADER = new ComputerLoader(FINDER, Mockito.mock(StaticEngine.class));

    @BeforeClass
    public static void init() {
        final DTreeMixedCalculation tree = new DTreeMixedCalculation(TREE_ID, "Simple");
        {
            final Decider d = new BTreePlainCondition(D_ID, "b != 0", false);
            final Computer c = new DTreePlainCalculation(C_ID, "d = a/b");
            final DTreeDecisionNode decision = new DTreeDecisionNode(D_ID, tree, 0, d, false);
            tree.getNodes().add(decision);
            tree.getNodes().add(new DTreeRuleNode(C_ID, decision, 0, c));
        }
        Mockito.when(FINDER.find(TREE_ID)).thenReturn(tree);
        Mockito.when(FINDER.find(CALC_ID)).thenReturn(new DTreePlainCalculation(CALC_ID, "a = b"));
        Mockito.when(FINDER.find(Long.MAX_VALUE)).thenReturn(null);
    }

    @Test(expected = LoadingException.class)
    public void loadUnknown() {
        // When
        LOADER.load(Long.MAX_VALUE);

        // Then BOOM
    }

    @Test
    public void shouldCache() {
        // When
        final Calculation tree = LOADER.load(CALC_ID);
        final Calculation cached = LOADER.load(CALC_ID);

        // Then
        assertSame(tree, cached);
    }

    @Test
    public void shouldCacheTree() {
        // When
        final Calculation tree = LOADER.load(TREE_ID);
        final Calculation cached = LOADER.load(TREE_ID);

        // Then
        assertSame(tree, cached);
    }

    @Test
    public void shouldEvict() {
        // When
        final Calculation tree = LOADER.load(CALC_ID);
        LOADER.evict(CALC_ID);
        final Calculation cached = LOADER.load(CALC_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldEvictTree() {
        // When
        final Calculation tree = LOADER.load(TREE_ID);
        LOADER.evict(TREE_ID);
        final Calculation cached = LOADER.load(TREE_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldClear() {
        // When
        final Calculation tree = LOADER.load(CALC_ID);
        LOADER.evict();
        final Calculation cached = LOADER.load(CALC_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldClearTree() {
        // When
        final Calculation tree = LOADER.load(TREE_ID);
        LOADER.evict();
        final Calculation cached = LOADER.load(TREE_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldCacheNodes() {
        // Given
        // When
        final Calculation tree = LOADER.load(TREE_ID);

        // Then
        assertThat(tree)
                .extracting("tree")
                .isNotNull()
                .isInstanceOf(DTree.class)
                .hasFieldOrPropertyWithValue("children.size", 1)
                .extracting(t -> ((DTree) t).childAt(0))
                .hasFieldOrPropertyWithValue("condition.calleeId", 1002L)
                .hasFieldOrProperty("condition.engine")
                .hasFieldOrPropertyWithValue("children.size", 1)
                .extracting(t -> t.childAt(0))
                .hasFieldOrPropertyWithValue("rule.calleeId", C_ID)
                .hasFieldOrProperty("rule.engine")
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test
    public void shouldClearDependencies() {
        // Given
        LOADER.load(TREE_ID);
        final Condition d = Decision.of(BaseScript.class, D_ID, "a != 0");

        // When
        LOADER.evict(TREE_ID);
        LOADER.load(TREE_ID);
        final Condition d1 = Condition.of(BaseScript.class, D_ID, "a != 0");

        // Then
        assertNotSame(d, d1);
    }

    @Test
    public void shouldIgnoreUnknownDecisionNode() {
        // Given
        final Condition condition = Mockito.mock(Condition.class);
        final Decision decision = Mockito.mock(Decision.class);
        Mockito.when(decision.getCondition()).thenReturn(condition);
        Mockito.when(decision.isDefault()).thenReturn(false);
        Mockito.when(decision.iterator()).thenReturn(Collections.emptyListIterator());

        // Then
        ComputerLoader.remove(decision);
    }

    @Test
    public void shouldIgnoreDefaultNode() {
        // Given
        final Decision decision = Mockito.mock(Decision.class);
        Mockito.when(decision.isDefault()).thenReturn(true);
        Mockito.when(decision.iterator()).thenReturn(Collections.emptyListIterator());

        // Then
        ComputerLoader.remove(decision);
    }

    @Test
    public void shouldIgnoreUnknownRuleNode() {
        // Given
        final Calculation calculation = Mockito.mock(Calculation.class);
        final Rule rule = Mockito.mock(Rule.class);
        Mockito.when(rule.getCalculation()).thenReturn(calculation);
        Mockito.when(rule.iterator()).thenReturn(Collections.emptyListIterator());

        // Then
        ComputerLoader.remove(rule);
    }

    @Test
    public void shouldIgnoreUnknownDNode() {
        // Given
        final Node node = Mockito.mock(Node.class);
        Mockito.when(node.iterator()).thenReturn(Collections.emptyListIterator());

        // Then
        ComputerLoader.remove(node);
    }

    @Test
    public void loadPlain() {
        // Given
        final long id = 1003;

        // When
        final Calculation loaded = LOADER.load(id);

        // Then
        assertThat(loaded)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrProperty("checksum")
                .hasFieldOrPropertyWithValue("script", "a = b");
    }

    @Test
    public void loadEmpty() {
        // Given
        final long id = 1000;

        // When
        final Calculation loaded = LOADER.load(id);

        // Then
        assertThat(loaded)
                .isNotNull()
                .hasFieldOrPropertyWithValue("tree.name", "Simple")
                .hasFieldOrPropertyWithValue("tree.children.size", 1);
    }

}
