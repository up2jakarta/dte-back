package io.github.up2jakarta.dte.exe.engine;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.exe.engine.dtree.Node;
import io.github.up2jakarta.dte.exe.engine.dtree.Rule;
import io.github.up2jakarta.dte.exe.script.ExecutionException;
import io.github.up2jakarta.dte.exe.script.StaticScript;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.*;

/**
 * Test cases for Parsers implementations
 */
@RunWith(JUnit4.class)
public class ParserTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void RuleTrue() {
        // Given
        context.put("b", false);
        final Rule formula = Rule.of(TestHelper.calculation("b = true"));

        // when
        final Map<String, Object> result = formula.resolve(context);

        // then
        assertNotNull(result);
        assertEquals(true, result.get("b"));
    }

    @Test(expected = RuntimeException.class)
    public void RuleException() {
        // Given
        context.put("b", false);
        final Rule formula = Rule.of(TestHelper.calculation("b += 1"));

        // when
        formula.resolve(context);

        // then
        // BOOM
    }

    @Test
    public void RuleMissingProperty() {
        // Given
        final Rule formula = Rule.of(TestHelper.calculation("b = !b"));
        context.put("b", false);

        // when
        final Map<String, Object> result = formula.resolve(context);

        // then
        assertNotNull(result);
        assertNotNull(result.get("b"));
        assertTrue((Boolean) result.get("b"));
    }

    @Test(expected = RuntimeException.class)
    public void RuleError() {
        // Given
        final Rule formula = Rule.of(TestHelper.calculation("1 /+"));

        // when
        formula.resolve(context);

        // then
        // BOOM
    }

    @Test
    public void defaultNode() {
        // Given
        final Node<?> node = Decision.empty();

        // when

        // then
        assertThat(node).hasFieldOrPropertyWithValue("default", true);
    }

    @Test
    public void localFunctions() {
        // Given
        final String script = "def x() { return 100}\n" +
                "def boolean exists(String var) { return false}\n" +
                "a = exists('vv')\n  b = x()";
        // when
        context.put("vv", true);
        final Map<?, ?> result = TestHelper.calculation(script).resolve(context);

        // then
        assertNotNull(result);
        assertEquals(false, result.get("a"));
        assertEquals(100, result.get("b"));
    }

    @Test
    public void noCacheClasses() {
        // GIVEN
        final StaticScript script1 = TestHelper.LOADER.parse("S00", "a00 = b00;");
        final StaticScript script2 = TestHelper.LOADER.parse("S00", "a00 = b00;");

        // WHEN
        Object class1 = TestHelper.extract("scriptClass", script1);
        Object class2 = TestHelper.extract("scriptClass", script2);

        //THEN
        assertNotSame(class1, class2);
    }

    @Test
    public void notCacheClassesWhitDistinctKeys() {
        // GIVEN
        final StaticScript script1 = TestHelper.LOADER.parse("S22", "a22 = c22");
        final StaticScript script2 = TestHelper.LOADER.parse("S21", "a22 = c22");

        // WHEN
        Object class1 = TestHelper.extract("scriptClass", script1);
        Object class2 = TestHelper.extract("scriptClass", script2);

        //THEN
        assertNotSame(class1, class2);
    }

    @Test
    public void ConditionTrue() {
        // Given
        final Condition condition = TestHelper.condition("true");

        // when
        boolean result = condition.isFulfilled(context);

        // then
        assertTrue(result);
    }

    @Test
    public void ConditionFalse() {
        // Given
        final Condition condition = TestHelper.condition("false");

        // when
        boolean result = condition.isFulfilled(context);

        // then
        assertFalse(result);
    }

    @Test
    public void ConditionEmpty() {
        // Given
        final Condition condition = TestHelper.condition("");

        // when
        boolean result = condition.isFulfilled(context);

        // then
        assertFalse(result);
    }

    @Test(expected = NullPointerException.class)
    public void ConditionNull() {
        // Given
        final Condition condition = TestHelper.condition(null);

        // when
        condition.isFulfilled(context);

        // then
        // BOOM
    }

    @Test
    public void ConditionNullCondition() {
        // Given
        final Condition condition = TestHelper.condition("null");

        // when
        boolean result = condition.isFulfilled(context);

        // then
        assertFalse(result);
    }

    @Test
    public void ConditionIntZero() {
        // Given
        final Condition condition = TestHelper.condition("0");

        // when
        boolean result = condition.isFulfilled(context);

        // then
        assertFalse(result);
    }

    @Test
    public void ConditionIntOne() {
        // Given
        final Condition condition = TestHelper.condition("1");

        // when
        boolean result = condition.isFulfilled(context);

        // then
        assertTrue(result);
    }

    @Test(expected = RuntimeException.class)
    public void ConditionIntOther() {
        // Given
        final Condition condition = TestHelper.condition("1005884561");

        // when
        condition.isFulfilled(context);

        // then
        // BOOM
    }

    @Test(expected = RuntimeException.class)
    public void ConditionObjectError() {
        // Given
        final Condition condition = TestHelper.condition("\"a\"");

        // when
        condition.isFulfilled(context);

        // then
        // BOOM
    }

    @Test(expected = RuntimeException.class)
    public void ConditionSyntaxError() {
        // Given
        final Condition condition = TestHelper.condition("a /+");

        // when
        condition.isFulfilled(context);

        // then
        // BOOM
    }

    @Test(expected = RuntimeException.class)
    public void ConditionExecutionError() {
        // Given
        final Condition condition = TestHelper.condition(" true / 1");

        // when
        condition.isFulfilled(context);

        // then
        // BOOM
    }

    @Test
    public void ConditionEmptyExecution() {
        // GIVEN
        final BTree tree = BTree.of("Test", Operator.AND);
        final Condition bool = Condition.of(tree);

        // WHEN
        final boolean result = bool.isFulfilled(Collections.emptyMap());

        // THEN BOOM
        assertTrue(result);
    }


    @Test
    public void ConditionReturnIntBool1() {
        // GIVEN
        final Condition bool = TestHelper.condition("test");

        // WHEN
        boolean result = bool.isFulfilled(Collections.singletonMap("test", 1));
        // THEN
        assertTrue(result);
    }

    @Test
    public void ConditionReturnIntBool2() {
        // GIVEN
        final Condition bool = TestHelper.condition("test");

        // WHEN
        boolean result = bool.isFulfilled(Collections.singletonMap("test", 0));
        // THEN
        assertFalse(result);
    }

    @Test(expected = ExecutionException.class)
    public void ConditionInvalidReturnInt() {
        // GIVEN
        final Condition bool = TestHelper.condition("x");

        // WHEN
        bool.isFulfilled(Collections.singletonMap("x", 2));
        // THEN BOOM
    }

    @Test(expected = ExecutionException.class)
    public void ConditionInvalidReturn() {
        // GIVEN
        final Condition bool = TestHelper.condition("x");

        // WHEN
        bool.isFulfilled(Collections.singletonMap("x", ""));
        // THEN BOOM
    }

}
