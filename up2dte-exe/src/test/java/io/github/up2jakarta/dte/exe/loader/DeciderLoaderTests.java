package io.github.up2jakarta.dte.exe.loader;

import org.assertj.core.api.AbstractObjectAssert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.TestHelper;
import io.github.up2jakarta.dte.exe.engine.btree.Leaf;
import io.github.up2jakarta.dte.exe.engine.btree.Operand;
import io.github.up2jakarta.dte.exe.loader.impl.BTreeLinkNode;
import io.github.up2jakarta.dte.exe.loader.impl.BTreeMixedCondition;
import io.github.up2jakarta.dte.exe.loader.impl.BTreePlainCondition;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertNotSame;
import static org.junit.Assert.assertSame;

public class DeciderLoaderTests {

    private static final long TREE_ID = 1000;
    private static final long A_ID = 1001;
    private static final long B_ID = 1002;
    private static final long BOOL_ID = 1003;

    private static final Finder<Decider> FINDER = TestHelper.mockFinder(Decider.class);
    private static final DeciderLoader LOADER = new DeciderLoader(FINDER, Mockito.mock(StaticEngine.class));

    @BeforeClass
    public static void init() {
        final BTreeMixedCondition tree = new BTreeMixedCondition(TREE_ID, "a AND b", Operator.AND, false);
        {
            final Decider a = new BTreePlainCondition(A_ID, "a", false);
            final Decider b = new BTreePlainCondition(B_ID, "b", false);
            tree.getNodes().add(new BTreeLinkNode(1021, tree, 0, a, false));
            tree.getNodes().add(new BTreeLinkNode(1021, tree, 1, b, true));
        }

        Mockito.when(FINDER.find(TREE_ID)).thenReturn(tree);
        Mockito.when(FINDER.find(BOOL_ID)).thenReturn(new BTreePlainCondition(BOOL_ID, "a == b", false));
        Mockito.when(FINDER.find(0)).thenReturn(null);
        Mockito.when(FINDER.find(Long.MAX_VALUE)).thenReturn(null);
        Mockito.when(FINDER.find(1004)).thenReturn(new BTreeMixedCondition(101, "Plain", Operator.AND, false));

    }

    @Test(expected = LoadingException.class)
    public void loadUnknown() {
        // When
        LOADER.load(Long.MAX_VALUE);

        // Then BOOM
    }

    @Test(expected = LoadingException.class)
    public void shouldNotCacheNull() {
        LOADER.load(0);
    }

    @Test
    public void shouldCache() {
        // When
        final Condition tree = LOADER.load(BOOL_ID);
        final Condition cached = LOADER.load(BOOL_ID);

        // Then
        assertSame(tree, cached);
    }

    @Test
    public void shouldCacheTree() {
        // When
        final Condition tree = LOADER.load(TREE_ID);
        final Condition cached = LOADER.load(TREE_ID);

        // Then
        assertSame(tree, cached);
    }

    @Test
    public void shouldEvict() {
        // When
        final Condition tree = LOADER.load(BOOL_ID);
        LOADER.evict(BOOL_ID);
        final Condition cached = LOADER.load(BOOL_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldEvictTree() {
        // When
        final Condition tree = LOADER.load(TREE_ID);
        LOADER.evict(TREE_ID);
        final Condition cached = LOADER.load(TREE_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldClear() {
        // When
        final Condition tree = LOADER.load(BOOL_ID);
        LOADER.evict();
        final Condition cached = LOADER.load(BOOL_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    public void shouldClearTree() {
        // When
        final Condition tree = LOADER.load(TREE_ID);
        LOADER.evict();
        final Condition cached = LOADER.load(TREE_ID);

        // Then
        assertNotSame(tree, cached);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void shouldCacheNodes() {
        // Given
        // When
        final Condition tree = LOADER.load(TREE_ID);

        // Then
        final AbstractObjectAssert operands = assertThat(tree)
                .extracting("tree")
                .isNotNull()
                .isInstanceOf(Operand.class)
                .hasFieldOrPropertyWithValue("operands.size", 2)
                .extracting("operands");

        operands.extracting(t -> ((List<Leaf>) t).get(0)).hasFieldOrPropertyWithValue("condition.calleeId", A_ID);
        operands.extracting(t -> ((List<Leaf>) t).get(1)).hasFieldOrPropertyWithValue("condition.calleeId", B_ID);
    }

    @Test
    public void loadPlain() {
        // Given
        final long id = BOOL_ID;

        // When
        final Condition loaded = LOADER.load(id);

        // Then
        assertThat(loaded)
                .isNotNull()
                .hasFieldOrPropertyWithValue("id", id)
                .hasFieldOrProperty("checksum")
                .hasFieldOrPropertyWithValue("script", "a == b");
    }

    @Test
    public void loadEmpty() {
        // Given
        final long id = 1004;

        // When
        final Condition loaded = LOADER.load(id);

        // Then
        assertThat(loaded)
                .isNotNull()
                .hasFieldOrPropertyWithValue("default", true)
                .hasFieldOrPropertyWithValue("children.size", 0);
    }

    @Test
    public void loadTree() {
        // Given
        final long id = 1000;

        // When
        final Condition loaded = LOADER.load(id);

        // Then BOOM
        assertThat(loaded)
                .isNotNull()
                .hasFieldOrPropertyWithValue("tree.name", "a AND b")
                .hasFieldOrPropertyWithValue("tree.operands.size", 2)
                .hasFieldOrPropertyWithValue("tree.negated", false)
                .hasFieldOrPropertyWithValue("tree.operator", Operator.AND)
        ;
    }
}
