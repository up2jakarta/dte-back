package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for OffsetTime operations.
 */
@RunWith(JUnit4.class)
public class OffsetTimeTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneOffset offset = ZoneOffset.of("+01:30");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void offsetTimePlusNumber() {
        context.put("t", OffsetTime.of(1, 30, 0, 0, offset));
        context.put("n", 9);
        final String code = "t + n";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(1, 30, 9, 0, offset), p);
    }

    @Test
    public void offsetTimeMinusNumber() {
        context.put("t", OffsetTime.of(1, 30, 30, 0, offset));
        context.put("n", 5);
        final String code = "t - n";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(1, 30, 25, 0, offset), p);
    }

    @Test
    public void offsetTimeMinusOffsetTime() {
        context.put("t", OffsetTime.of(20, 10, 5, 2, offset));
        context.put("b", OffsetTime.of(19, 5, 5, 2, offset));

        final Duration p = (Duration) eval("t - b", context);
        assertEquals(Duration.ofHours(1).plusMinutes(5), p);

        context.put("d", p);
        assertEquals(true, eval("t == b + d", context));
    }

    @Test
    public void offsetTimeNext() {
        context.put("t", OffsetTime.of(1, 30, 0, 0, offset));
        final String code = "++t";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(1, 30, 1, 0, offset), p);
    }

    @Test
    public void offsetTimePrevious() {
        context.put("t", OffsetTime.of(1, 30, 0, 0, offset));
        final String code = "--t";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(1, 29, 59, 0, offset), p);
    }

    @Test
    public void offsetTimeCombineWithLocalDate() {
        context.put("t", OffsetTime.of(1, 30, 0, 0, offset));
        context.put("d", LocalDate.of(2020, 9, 3));
        final String code = "t | d";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset), p);
    }
}
