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
 * Test cases for Duration operations.
 */
@RunWith(JUnit4.class)
public class DurationTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneOffset offset = ZoneOffset.of("+01:30");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void durationPlusLocalTime() {
        context.put("d", Duration.ofDays(1));
        context.put("t", LocalTime.of(20, 1, 1));
        final String code = "d + t";
        final LocalTime p = (LocalTime) eval(code, context);
        assertEquals(LocalTime.of(20, 1, 1), p);
    }

    @Test
    public void durationPlusOffsetTime() {
        context.put("d", Duration.ofDays(1));
        context.put("t", OffsetTime.of(20, 1, 1, 0, offset));
        final String code = "d + t";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(20, 1, 1, 0, offset), p);
    }

    @Test
    public void durationPlusLocalDateTime() {
        context.put("d", Duration.ofHours(1));
        context.put("t", LocalDateTime.of(1999, 1, 1, 1, 1, 1));
        final String code = "d + t";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(1999, 1, 1, 2, 1, 1), p);
    }

    @Test
    public void durationPlusOffsetDateTime() {
        context.put("d", Duration.ofHours(1));
        context.put("t", OffsetDateTime.of(1999, 1, 1, 1, 1, 1, 0, offset));
        final String code = "d + t";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(1999, 1, 1, 2, 1, 1, 0, offset), p);
    }

    @Test
    public void durationPlusZonedDateTime() {
        context.put("d", Duration.ofHours(1));
        context.put("t", ZonedDateTime.of(1999, 1, 1, 1, 1, 1, 0, offset));
        final String code = "d + t";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(1999, 1, 1, 2, 1, 1, 0, offset), p);
    }

    @Test
    public void durationPlusDuration() {
        context.put("a", Duration.ofHours(1));
        context.put("b", Duration.ofMinutes(1));
        final String code = "a + b";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofMinutes(61), d);
    }

    @Test
    public void durationMinusDuration() {
        context.put("a", Duration.ofHours(1));
        context.put("b", Duration.ofMinutes(1));
        final String code = "a - b";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofMinutes(59), d);
    }

    @Test
    public void durationPlusSeconds() {
        context.put("a", Duration.ofMinutes(1));
        context.put("b", 1);
        final String code = "a + b";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(61), d);
    }

    @Test
    public void durationMinusSeconds() {
        context.put("a", Duration.ofMinutes(1));
        context.put("b", 1);
        final String code = "a - b";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(59), d);
    }

    @Test
    public void durationNext() {
        context.put("a", Duration.ofSeconds(20));
        final String code = "++a";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(21), d);
    }

    @Test
    public void durationPrevious() {
        context.put("a", Duration.ofSeconds(20));
        final String code = "--a";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(19), d);
    }

    @Test
    public void durationNegative() {
        context.put("a", Duration.ofSeconds(20));
        final String code = "-a";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(-20), d);
    }

    @Test
    public void durationPositive() {
        context.put("a", Duration.ofSeconds(-20));
        final String code = "+a";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(20), d);
    }

    @Test
    public void durationMultiply() {
        context.put("a", Duration.ofSeconds(20));
        final String code = "a * 2";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(40), d);
    }

    @Test
    public void durationDivide() {
        context.put("a", Duration.ofSeconds(20));
        final String code = "a / 2";

        final Duration d = (Duration) eval(code, context);

        assertEquals(Duration.ofSeconds(10), d);
    }

}
