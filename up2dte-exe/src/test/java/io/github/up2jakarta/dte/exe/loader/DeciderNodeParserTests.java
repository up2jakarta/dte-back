package io.github.up2jakarta.dte.exe.loader;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.api.btree.ChildNode;
import io.github.up2jakarta.dte.exe.engine.TestHelper;
import io.github.up2jakarta.dte.exe.engine.btree.Node;
import io.github.up2jakarta.dte.exe.loader.impl.BTreeLinkNode;
import io.github.up2jakarta.dte.exe.loader.impl.BTreeMixedCondition;
import io.github.up2jakarta.dte.exe.loader.impl.BTreeOperandNode;
import io.github.up2jakarta.dte.exe.loader.impl.BTreePlainCondition;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for Condition Parser implementations
 */
@RunWith(JUnit4.class)
public class DeciderNodeParserTests {

    private static final Decider PLAIN = new BTreePlainCondition(100, "a == b", false);
    private static final BTreeMixedCondition MIXED = new BTreeMixedCondition(101, "Plain", Operator.AND, false);
    private static final Finder<Decider> FINDER = id -> {
        if (id == 100) {
            return PLAIN;
        }
        if (id == 101) {
            return MIXED;
        }
        if (id == 102) {
            final BTreeMixedCondition tree = new BTreeMixedCondition(102, "False", Operator.AND, false);
            tree.getNodes().add(new BTreeLinkNode(1021, tree, 0, PLAIN, false));
            tree.getNodes().add(new BTreeLinkNode(1021, tree, 1, PLAIN, true));
            return tree;
        }
        return null;
    };

    private static final StaticEngine ENGINE = new StaticEngine(TestHelper.mockFinder(Computer.class), FINDER);
    private static final DeciderNodeParser PARSER = new DeciderNodeParser(ENGINE);

    @Test
    public void parseParentNode() {
        // Given
        final BTreeOperandNode parent = new BTreeOperandNode(2, MIXED, 0, Operator.AND, false);

        // when
        final Node node = PARSER.parse(parent);

        // then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("operator", Operator.AND)
                .hasFieldOrPropertyWithValue("operands.size", 0);
    }

    @Test
    public void parseLeafNode() {
        // Given
        final BTreeLinkNode leaf = new BTreeLinkNode(3, MIXED, 0, PLAIN, false);

        // when
        final Node node = PARSER.parse(leaf);

        // then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("condition.calleeId", PLAIN.getId())
                .hasFieldOrProperty("condition.engine");
    }

    @Test
    public void parsePlainLinkNode() {
        // Given
        final BTreeLinkNode leaf = new BTreeLinkNode(4, MIXED, 0, PLAIN, false);

        // when
        final Node node = PARSER.parse(leaf);

        // then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrProperty("condition.engine")
                .hasFieldOrPropertyWithValue("condition.calleeId", PLAIN.getId());
    }

    @Test
    public void parseMixedLinkNode() {
        // Given
        final BTreeLinkNode leaf = new BTreeLinkNode(3, MIXED, 0, MIXED, true);

        // when
        final Node node = PARSER.parse(leaf);

        // then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("condition.engine", ENGINE)
                .hasFieldOrPropertyWithValue("condition.calleeId", leaf.getDecider().getId());
    }

    @Test(expected = IllegalStateException.class)
    public void parseInvalidCondition() {
        // Given
        final Decider decider = Mockito.mock(Decider.class);
        final BTreeLinkNode leaf = new BTreeLinkNode(3, MIXED, 0, decider, true);

        // when
        Mockito.when(decider.getId()).thenThrow(new IllegalStateException("Test"));
        PARSER.parse(leaf);

        // then BOOM
    }

    @Test(expected = ParsingException.class)
    public void parseInvalidChild() {
        // Given
        final ChildNode child = Mockito.mock(ChildNode.class);

        // when
        PARSER.parse(child);

        // then BOOM
    }
}
