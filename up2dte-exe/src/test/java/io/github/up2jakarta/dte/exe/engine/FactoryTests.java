package io.github.up2jakarta.dte.exe.engine;

import org.junit.Assert;
import org.junit.Test;
import io.github.up2jakarta.dte.dsl.BaseScript;

import java.util.Collections;

public class FactoryTests {

    @Test
    public void conditionEquality() {
        // when
        final Condition bool1 = ConditionFactory.of(BaseScript.class, 1, "test");
        final Condition bool2 = ConditionFactory.of(BaseScript.class, 1, "test");
        final Condition bool3 = DecisionFactory.of(BaseScript.class, 0, "test");

        // then
        Assert.assertEquals(bool1, bool2);
        Assert.assertNotEquals(bool1, bool3);
        Assert.assertNotEquals(bool2, bool3);
    }

    @Test
    public void shouldCacheBool() {
        // when
        final Condition bool1 = ConditionFactory.of(BaseScript.class, -Long.MAX_VALUE, "true");
        Assert.assertTrue(bool1.isFulfilled(Collections.emptyMap()));

        // then
        final Condition bool2 = ConditionFactory.of(BaseScript.class, -Long.MAX_VALUE, "false");
        Assert.assertSame(bool1, bool2);
        Assert.assertFalse(bool1.isFulfilled(Collections.emptyMap()));
        Assert.assertFalse(bool2.isFulfilled(Collections.emptyMap()));
    }

    @Test
    public void evictCalculation() {
        // when
        CalculationFactory.of(BaseScript.class, -100, "a = b");

        // then
        CalculationFactory.evict(-100);
        CalculationFactory.evict(100);
    }

    @Test
    public void testCache() {
        // Given
        final long id = Long.MAX_VALUE - 1;
        // when
        final Condition condition = ConditionFactory.of(BaseScript.class, id, "true");
        final Calculation calculation = CalculationFactory.of(BaseScript.class, id, "test = 1");
        final Condition decision = DecisionFactory.of(BaseScript.class, id, "false");

        // then
        Assert.assertTrue(condition.isFulfilled(Collections.emptyMap()));
        Assert.assertFalse(decision.isFulfilled(Collections.emptyMap()));
        Assert.assertEquals(1, calculation.resolve(Collections.emptyMap()).get("test"));

        // then also
        Assert.assertTrue(condition.isFulfilled(Collections.emptyMap()));
        Assert.assertFalse(decision.isFulfilled(Collections.emptyMap()));
        Assert.assertEquals(1, calculation.resolve(Collections.emptyMap()).get("test"));
    }
}
