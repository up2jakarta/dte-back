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
 * Test cases for LocalTime operations.
 */
@RunWith(JUnit4.class)
public class LocalTimeTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void localTimePlusNumber() {
        context.put("t", LocalTime.of(20, 10, 0));
        context.put("n", 5);
        final String code = "t + n";
        final LocalTime p = (LocalTime) eval(code, context);
        assertEquals(LocalTime.of(20, 10, 5), p);
    }

    @Test
    public void localTimeMinusNumber() {
        context.put("t", LocalTime.of(20, 10, 5));
        context.put("n", 5);
        final String code = "t - n";
        final LocalTime p = (LocalTime) eval(code, context);
        assertEquals(LocalTime.of(20, 10, 0), p);
    }

    @Test
    public void localTimeMinusLocalTime() {
        context.put("t", LocalTime.of(20, 10, 5));
        context.put("n", LocalTime.of(10, 5, 5));

        final Duration p = (Duration) eval("t - n", context);
        assertEquals(Duration.ofHours(10).plusMinutes(5), p);

        context.put("d", p);
        assertEquals(true, eval("t == n + d", context));
    }

    @Test
    public void localTimeNext() {
        context.put("t", LocalTime.of(20, 10, 0));
        final String code = "++t";
        final LocalTime p = (LocalTime) eval(code, context);
        assertEquals(LocalTime.of(20, 10, 1), p);
    }

    @Test
    public void localTimePrevious() {
        context.put("t", LocalTime.of(20, 10, 5));
        final String code = "--t";
        final LocalTime p = (LocalTime) eval(code, context);
        assertEquals(LocalTime.of(20, 10, 4), p);
    }

    @Test
    public void localTimeCombineWithLocalDate() {
        context.put("t", LocalTime.of(20, 10, 5));
        context.put("d", LocalDate.of(2020, 9, 3));
        final String code = "t | d";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 9, 3, 20, 10, 5), p);
    }

    @Test
    public void localTimeCombineWithZoneOffset() {
        context.put("t", LocalTime.of(20, 10, 5));
        context.put("d", ZoneOffset.of("+01:30"));
        final String code = "t | d";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(20, 10, 5, 0, ZoneOffset.of("+01:30")), p);
    }
}
