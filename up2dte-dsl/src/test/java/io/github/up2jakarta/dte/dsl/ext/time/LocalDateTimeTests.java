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
 * Test cases for LocalDateTime operations.
 */
@RunWith(JUnit4.class)
public class LocalDateTimeTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void localDateTimePlusNumber() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        context.put("n", 5);
        final String code = "t + n";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 14, 40, 25), p);
    }

    @Test
    public void localDateTimePlusDuration() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        context.put("n", Duration.ofHours(5));
        final String code = "t + n";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 19, 40, 20), p);
    }

    @Test
    public void localDateTimePlusPeriod() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        final String code = "t + 2.days";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 4, 14, 40, 20), p);
    }

    @Test
    public void localDateTimeMinusDuration() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        context.put("n", Duration.ofHours(5));
        final String code = "t - n";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 9, 40, 20), p);
    }

    @Test
    public void localDateTimeMinusPeriod() {
        context.put("t", LocalDateTime.of(2020, 10, 4, 14, 40, 20));
        final String code = "t - 2.days";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 14, 40, 20), p);
    }

    @Test
    public void localDateTimeMinusNumber() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        context.put("n", 5);
        final String code = "t - n";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 14, 40, 15), p);
    }

    @Test
    public void localDateTimeMinusLocalDateTime() {
        context.put("t", LocalDateTime.of(20, 10, 6, 14, 40, 20));
        context.put("n", LocalDateTime.of(20, 10, 1, 4, 40, 20));

        final Duration p = (Duration) eval("t - n", context);
        assertEquals(Duration.ofHours(10).plusDays(5), p);

        context.put("d", p);
        assertEquals(true, eval("t == n + d", context));
    }

    @Test
    public void localDateTimeNext() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        final String code = "++t";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 14, 40, 21), p);
    }

    @Test
    public void localDateTimePrevious() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        final String code = "--t";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 10, 2, 14, 40, 19), p);
    }

    @Test
    public void localDateTimeCombineWithZoneId() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        context.put("d", ZoneId.of("Africa/Tunis"));
        final String code = "t | d";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 10, 2, 14, 40, 20, 0, ZoneId.of("Africa/Tunis")), p);
    }

    @Test
    public void localDateTimeCombineWithZoneOffset() {
        context.put("t", LocalDateTime.of(2020, 10, 2, 14, 40, 20));
        context.put("d", ZoneOffset.of("+01:30"));
        final String code = "t | d";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 10, 2, 14, 40, 20, 0, ZoneOffset.of("+01:30")), p);
    }

    @Test
    public void localDateTimeBitwiseNegate() {
        final Instant moment = Instant.now();
        context.put("t", moment.atZone(ZoneId.systemDefault()).toLocalDateTime());
        final String code = "~t";
        final Instant p = (Instant) eval(code, context);
        assertEquals(moment, p);
    }
}
