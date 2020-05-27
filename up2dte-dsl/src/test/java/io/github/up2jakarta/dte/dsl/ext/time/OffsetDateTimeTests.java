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
 * Test cases for OffsetDateTime operations.
 */
@RunWith(JUnit4.class)
public class OffsetDateTimeTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneOffset offset = ZoneOffset.of("+01:30");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void offsetDateTimePlusNumber() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset));
        context.put("n", 9);
        final String code = "t + n";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 30, 9, 0, offset), p);
    }

    @Test
    public void offsetDateTimePlusDuration() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 20, 30, 0, 0, offset));
        context.put("n", Duration.ofHours(5));
        final String code = "t + n";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 4, 1, 30, 0, 0, offset), p);
    }

    @Test
    public void offsetDateTimePlusPeriod() {
        context.put("t", OffsetDateTime.of(2020, 9, 30, 1, 30, 9, 0, offset));
        final String code = "t + 2.days";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 10, 2, 1, 30, 9, 0, offset), p);
    }

    @Test
    public void offsetDateTimeMinusDuration() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 9, 0, offset));
        context.put("n", Duration.ofHours(5));
        final String code = "t - n";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 2, 20, 30, 9, 0, offset), p);
    }

    @Test
    public void offsetDateTimeMinusPeriod() {
        context.put("t", OffsetDateTime.of(2020, 10, 2, 1, 30, 9, 0, offset));
        final String code = "t - 2.days";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 30, 1, 30, 9, 0, offset), p);
    }

    @Test
    public void offsetDateTimeMinusNumber() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset));
        context.put("n", 5);
        final String code = "t - n";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 29, 55, 0, offset), p);
    }

    @Test
    public void offsetDateTimeMinusOffsetDateTime() {
        final LocalDateTime dateTime = LocalDateTime.of(2020, 9, 8, 23, 30, 9, 0);
        context.put("p", OffsetDateTime.of(dateTime, ZoneOffset.ofHours(2)));
        context.put("t", OffsetDateTime.of(dateTime, ZoneOffset.ofHours(1)));

        final Duration p = (Duration) eval("p - t", context);
        assertEquals(Duration.ofHours(-1), p);

        context.put("d", p);
        assertEquals(true, eval("~p == ~t + d", context));
    }

    @Test
    public void offsetDateTimeNext() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 59, 0, offset));
        final String code = "++t";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 31, 0, 0, offset), p);
    }

    @Test
    public void offsetDateTimePrevious() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset));
        final String code = "--t";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 29, 59, 0, offset), p);
    }

    @Test
    public void offsetDateTimeCombineWithZoneId() {
        final String code = "t | z";
        final OffsetDateTime date = OffsetDateTime.of(2020, 1, 3, 23, 30, 0, 0, offset);
        context.put("t", date);
        final ZoneId offset2 = ZoneId.of("Africa/Tunis");

        context.put("z", offset);
        ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 1, 3, 23, 30, 0, 0, offset), p);

        context.put("z", offset2);
        p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 1, 3, 23, 0, 0, 0, offset2), p);
    }

    @Test
    public void offsetDateTimeBitwiseNegate() {
        final Instant moment = Instant.now();
        context.put("t", moment.atOffset(ZoneOffset.UTC));
        final String code = "~t";
        final Instant p = (Instant) eval(code, context);
        assertEquals(moment, p);
    }
}
