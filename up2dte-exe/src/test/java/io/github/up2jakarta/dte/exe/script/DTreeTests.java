package io.github.up2jakarta.dte.exe.script;

import org.junit.Test;
import org.mockito.Mockito;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.engine.Calculation;
import io.github.up2jakarta.dte.exe.engine.Condition;
import io.github.up2jakarta.dte.exe.engine.DTree;
import io.github.up2jakarta.dte.exe.engine.Decision;
import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;
import io.github.up2jakarta.dte.exe.loader.ParsingException;

import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.Iterator;

import static org.junit.Assert.*;
import static io.github.up2jakarta.dte.exe.engine.TestHelper.calculation;
import static io.github.up2jakarta.dte.exe.engine.TestHelper.condition;

public class DTreeTests {

    @Test
    public void shouldSame() {
        // Given
        final DTree<Rule> tree = DTree.of("Test");
        final Rule r = Rule.of(calculation("f1 = 1"));

        // when
        tree.add(r);

        // then
        assertEquals(tree.childAt(0), r);
    }

    @Test(expected = ParsingException.class)
    public void shouldProxyError() {
        // Given
        final DTree<Rule> tree = DTree.of("Test");
        tree.add(Rule.of(calculation("f1 = 1")));
        final Rule rule = tree.add(Rule.of(calculation("f2 = 2")));

        // when
        rule.add(Decision.of(condition("a == b"), false));

        // then BOOM
    }

    @Test(expected = ParsingException.class)
    public void shouldError() {
        // Given
        final DTree<Rule> tree = DTree.of("Test");
        tree.add(Rule.of(calculation("f1 = 1")));

        // when
        final Rule rule = Rule.of(calculation("f2 = 2"));
        rule.add(Decision.of(condition("a == b"), false));
        tree.add(rule);

        // then BOOM
    }

    @Test(expected = IllegalStateException.class)
    public void shouldNotRemoveNode() {
        // Given
        final DTree<Decision<Rule>> tree = DTree.of("Test");
        final Condition bool = condition("a == b");
        tree.add(Decision.of(bool, false));

        // when
        final Iterator<Decision<Rule>> it = tree.iterator();
        while (it.hasNext()) {
            it.remove();
        }
        // then BOOM
    }

    @Test
    public void shouldAddDecisionNodes() {
        // Given
        final DTree<Decision<Rule>> tree = DTree.of("Test");
        final Condition bool = condition("a == b");
        tree.add(Decision.of(bool, false));
        final Decision<Rule> d = Decision.of(bool, true);

        // when
        tree.add(d);

        // then
        assertEquals(2, tree.size());
    }

    @Test
    public void shouldOneElseNode() {
        // Given
        final DTree<Decision<Rule>> tree = DTree.of("Test");
        tree.add(Decision.of(condition("a == b"), false));
        tree.add(Decision.of(condition("a == c"), false));

        // when
        tree.add(Decision.empty());

        // then
        assertEquals(3, tree.size());
    }

    @Test(expected = ParsingException.class)
    public void shouldOnlyOneElseNodes() {
        // Given
        final DTree<Decision<Rule>> tree = DTree.of("Test");
        tree.add(Decision.of(condition("a == b"), false));
        tree.add(Decision.empty());

        // when
        tree.add(Decision.empty());

        // then BOOM
    }

    @Test(expected = ParsingException.class)
    public void shouldElseNodeBeLast() {
        // Given
        final DTree<Decision<Rule>> tree = DTree.of("Test");
        tree.add(Decision.empty());
        final Decision<Rule> d = Decision.of(condition("a == b"), false);

        // when
        tree.add(d);

        // then BOOM
    }

    @Test
    public void shouldDefault() {
        // Given
        final Decision<Rule> decision = Decision.empty();

        // Then
        assertTrue(decision.isDefault());
        assertNull(decision.getCondition());
    }

    @Test
    public void shouldRuleToString() {
        // Given
        final Calculation calc = Calculation.of(BaseScript.class, 1, "test");
        final Rule rule = Rule.of(calc);

        // Then
        assertEquals(calc.toString(), rule.toString());
    }

    @Test(expected = NullPointerException.class)
    public void shouldProxyThrowOriginalException() {
        // Given
        final Decision<Rule> parent = Decision.empty();
        final Rule mock = Mockito.mock(Rule.class);
        Mockito.when(mock.isDefault()).thenThrow(new NullPointerException());

        // When
        parent.add(Rule.of(Calculation.of(BaseScript.class, 102, "102")));
        final Rule proxy = parent.add(mock);

        // Then
        proxy.isDefault();
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void shouldRuleWhenInvalidIndex() {
        // Given
        final Rule rule = Rule.of(Calculation.of(BaseScript.class, 1, "test"));

        // Then
        rule.last();
    }

    @Test
    public void shouldChildAtUnProxy() {
        // Given
        final Decision<Rule> parent = Decision.empty();
        final Rule last = Rule.of(Calculation.of(BaseScript.class, 103, "103"));
        parent.add(Rule.of(Calculation.of(BaseScript.class, 102, "102")));
        parent.add(last);

        // When
        final Object child = parent.childAt(1);

        // Then
        assertSame(last, child);
    }

    @Test(expected = ParsingException.class)
    @SuppressWarnings("unchecked")
    public void shouldNotAddRuleAfterDecision() {
        // Given
        final Decision parent = Decision.empty();
        parent.add(Decision.of(BaseScript.class, 101, "101"));

        // When
        parent.add(Rule.of(Calculation.of(BaseScript.class, 102, "102")));
    }

    @Test(expected = ParsingException.class)
    @SuppressWarnings("unchecked")
    public void shouldNotAddDecisionAfterRule() {
        // Given
        final Decision parent = Decision.empty();
        parent.add(Rule.of(Calculation.of(BaseScript.class, 102, "102")));

        // When
        parent.add(Decision.empty());
    }

    @Test(expected = ParsingException.class)
    public void shouldNotAddItself() {
        // Given
        final Decision<Decision<?>> parent = Decision.empty();

        // When
        parent.add(parent);
    }

    @Test(expected = ParsingException.class)
    @SuppressWarnings("unchecked")
    public void shouldNotAddRuleToRule() {
        // Given
        final Node parent = Rule.of(Calculation.of(BaseScript.class, 102, "102"));

        // When
        parent.add(Rule.of(Calculation.of(BaseScript.class, 102, "102")));
    }

    @Test
    public void shouldNotUnProxyUnknownProxy() {
        // Given
        final Decision<Rule> parent = Decision.empty();
        final Rule rule = Rule.of(Calculation.of(BaseScript.class, 102, "102"));
        final Rule proxyRule = (Rule) Proxy.newProxyInstance(
                Rule.class.getClassLoader(),
                new Class[]{Rule.class},
                (proxy, method, args) -> method.invoke(rule, args));

        // When
        parent.add(proxyRule);

        // Then
        assertSame(proxyRule, parent.childAt(0));
    }

    @Test
    public void shouldProxyUnknownProxy() {
        // Given
        final Decision<Rule> parent = Decision.empty();
        final Rule rule = Rule.of(Calculation.of(BaseScript.class, 102, "102"));
        final Rule proxy = (Rule) Proxy.newProxyInstance(
                Rule.class.getClassLoader(),
                new Class[]{Rule.class},
                (p, m, args) -> m.invoke(rule, args));

        // When
        parent.add(rule);
        parent.add(proxy);

        // Then
        assertSame(rule, parent.childAt(0));
        assertSame(proxy, parent.childAt(1));
    }

    @Test
    public void shouldNotProxyTwice() {
        // Given
        final Decision<Rule> parent = Decision.empty();
        parent.add(Rule.of(Calculation.of(BaseScript.class, 102, "102")));

        // When
        final Rule proxy1 = parent.add(Rule.of(Calculation.of(BaseScript.class, 103, "103")));
        final Rule proxy2 = parent.add(proxy1);

        // Then
        assertSame(proxy1, proxy2);
    }

    @Test
    public void shouldDecisionReturnTrue() {
        // When
        final Decision decision = Decision.of(Condition.of(BaseScript.class, 10002, "true"), false);

        // Then
        assertEquals("X#10002", decision.toString());
        assertTrue(decision.isFulfilled(Collections.emptyMap()));
    }

    @Test
    public void shouldDecisionReturnFalse() {
        // When
        final Decision decision = Decision.of(Condition.of(BaseScript.class, 98760, "true"), true);

        // Then
        assertEquals("!X#98760", decision.toString());
        assertFalse(decision.isFulfilled(Collections.emptyMap()));
    }

    @Test
    public void shouldDecisionWorks() {
        // When
        final Decision decision = Decision.of(BaseScript.class, 0, "false");

        // Then
        assertEquals("Y#0", decision.toString());
        assertFalse(decision.isFulfilled(Collections.emptyMap()));
    }

    @Test
    public void shouldSameRule() {
        // Given
        final Rule rule = Rule.of(Calculation.of(BaseScript.class, 0, "a"));

        // When
        final Rule rule1 = Rule.of(rule);

        // Then
        assertSame(rule, rule1);
    }

    @Test
    public void shouldSameDecision() {
        // Given
        final Decision decision = Decision.of(Condition.of(BaseScript.class, 98761, "a"), true);

        // When
        final Decision decision1 = Decision.of(decision, false);

        // Then
        assertSame(decision, decision1);
    }

    @Test
    public void shouldSameDecisionWhenNegated() {
        // Given
        final Decision decision = Decision.of(Condition.of(BaseScript.class, 98762, "a"), true);

        // When
        final Decision decision1 = Decision.of(decision, true);

        // Then
        assertSame(decision, decision1);
    }

    @Test
    public void shouldNodeSameDecisionWhenNegated() {
        // Given
        final Decision decision = Decision.of(BaseScript.class, 0, "a");

        // When
        final Decision decision1 = Decision.of(decision, true);

        // Then
        assertNotSame(decision, decision1);
    }

    @Test
    public void shouldCleanNotFirstRuleChildren() {
        // Given
        final Rule child = Rule.of(Calculation.of(BaseScript.class, 4002, "test"));
        final DTree<Rule> calc = DTree.of("Test");
        calc.add(Rule.of(Calculation.of(BaseScript.class, 4001, "test")));
        calc.add(child);

        // When
        child.add(Decision.of(BaseScript.class, 4003, "test"));
        DTree.clean(calc);

        // Then
        assertEquals(0, child.size());
    }

}
