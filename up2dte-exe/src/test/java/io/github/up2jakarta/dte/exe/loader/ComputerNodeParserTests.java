package io.github.up2jakarta.dte.exe.loader;

import io.github.up2jakarta.dte.exe.loader.impl.*;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.api.Computer;
import io.github.up2jakarta.dte.exe.api.Decider;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.api.dtree.ChildNode;
import io.github.up2jakarta.dte.exe.engine.TestHelper;
import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.loader.impl.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Test cases for Parsers implementations
 */
@RunWith(JUnit4.class)
public class ComputerNodeParserTests {

    private static final Decider PLAIN_CONDITION = new BTreePlainCondition(10, "a == b", false);
    private static final Decider MIXED_CONDITION = new BTreeMixedCondition(11, "Mixed", Operator.AND, false);
    private static final DTreePlainCalculation PLAIN = new DTreePlainCalculation(100, "a == b");
    private static final DTreeMixedCalculation MIXED = new DTreeMixedCalculation(101, "Mixed");
    private static final Finder<Computer> FINDER = id -> {
        if (id == 100) {
            return PLAIN;
        }
        if (id == 101) {
            return MIXED;
        }
        if (id == 102) {
            final DTreeMixedCalculation tree = new DTreeMixedCalculation(102, "Invalid");
            tree.getNodes().add(new DTreeRuleNode(1021, tree, 0, PLAIN));
            tree.getNodes().add(new DTreeDefaultNode(1022, tree, 1));
            return tree;
        }
        return null;
    };

    private static final StaticEngine ENGINE = new StaticEngine(FINDER, TestHelper.mockFinder(Decider.class));
    private static final ComputerNodeParser parser = new ComputerNodeParser(ENGINE);

    @Test
    public void parseDecisionNode() {
        // Given
        final DTreeDecisionNode d = new DTreeDecisionNode(1, MIXED, 0, PLAIN_CONDITION, false);

        // When
        final Node<?> node = parser.parse(d);

        // Then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("negated", false)
                .hasFieldOrPropertyWithValue("children.size", 0)
                .hasFieldOrProperty("condition.engine")
                .hasFieldOrPropertyWithValue("condition.calleeId", PLAIN_CONDITION.getId());
    }

    @Test
    public void parseRuleNode() {
        // Given
        final DTreeRuleNode calc = new DTreeRuleNode(2, MIXED, 0, PLAIN);

        // When
        final Node<?> node = parser.parse(calc);

        // Then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 0)
                .hasFieldOrProperty("rule.engine")
                .hasFieldOrPropertyWithValue("rule.calleeId", PLAIN.getId());
    }

    @Test
    public void parseDefaultNode() {
        // Given
        final DTreeDefaultNode def = new DTreeDefaultNode(7, MIXED, 0);

        // When
        final Node<?> node = parser.parse(def);

        // Then
        assertThat(node).hasFieldOrPropertyWithValue("default", true);
    }

    @Test
    public void parseComplexDecisionNode() {
        // Given
        final DTreeDecisionNode d = new DTreeDecisionNode(3, MIXED, 0, MIXED_CONDITION, true);

        // When
        final Node<?> node = parser.parse(d);

        // Then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("negated", true)
                .hasFieldOrPropertyWithValue("children.size", 0)
                .hasFieldOrPropertyWithValue("condition.calleeId", d.getDecider().getId())
                .hasFieldOrPropertyWithValue("condition.engine", ENGINE);
    }

    @Test
    public void parseComplexRuleNode() {
        // Given
        final DTreeRuleNode calc = new DTreeRuleNode(4, MIXED, 0, MIXED);

        // When
        final Node<?> node = parser.parse(calc);

        // Then
        assertThat(node)
                .isNotNull()
                .hasFieldOrPropertyWithValue("children.size", 0)
                .hasFieldOrPropertyWithValue("rule.calleeId", calc.getComputer().getId())
                .hasFieldOrPropertyWithValue("rule.engine", ENGINE);
    }

    @Test(expected = IllegalStateException.class)
    public void parseInvalidCondition() {
        // Given
        final Decider decider = Mockito.mock(Decider.class);
        final DTreeDecisionNode d = new DTreeDecisionNode(5, MIXED, 0, decider, true);

        // When
        Mockito.when(decider.getId()).thenThrow(new IllegalStateException("Test"));
        parser.parse(d);

        // Then BOOM
    }

    @Test(expected = IllegalStateException.class)
    public void parseInvalidCalculationNode() {
        // Given
        final Computer computer = Mockito.mock(Computer.class);
        final DTreeRuleNode calc = new DTreeRuleNode(6, MIXED, 0, computer);

        // When
        Mockito.when(computer.getId()).thenThrow(new IllegalStateException("Test"));
        parser.parse(calc);

        // Then BOOM
    }

    @Test(expected = ParsingException.class)
    public void parseInvalidNode() {
        // Given
        final ChildNode node = Mockito.mock(ChildNode.class);

        // When
        parser.parse(node);

        // Then BOOM
    }

}
