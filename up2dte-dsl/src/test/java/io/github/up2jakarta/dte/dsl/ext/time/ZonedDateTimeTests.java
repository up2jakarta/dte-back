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
 * Test cases for ZonedDateTime operations.
 */
@RunWith(JUnit4.class)
public class ZonedDateTimeTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneOffset zone = ZoneOffset.of("+01:00");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void zonedDateTimePlusNumber() {
        context.put("t", ZonedDateTime.of(2020, 9, 3, 1, 30, 0, 0, zone));
        context.put("n", 9);
        final String code = "t + n";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 3, 1, 30, 9, 0, zone), p);
    }

    @Test
    public void zonedDateTimePlusDuration() {
        context.put("t", ZonedDateTime.of(2020, 9, 3, 20, 30, 0, 0, zone));
        context.put("n", Duration.ofHours(5));
        final String code = "t + n";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 4, 1, 30, 0, 0, zone), p);
    }

    @Test
    public void zonedDateTimePlusPeriod() {
        context.put("t", ZonedDateTime.of(2020, 9, 30, 1, 30, 9, 0, zone));
        final String code = "t + 2.days";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 10, 2, 1, 30, 9, 0, zone), p);
    }

    @Test
    public void zonedDateTimeMinusDuration() {
        context.put("t", ZonedDateTime.of(2020, 9, 3, 1, 30, 9, 0, zone));
        context.put("n", Duration.ofHours(5));
        final String code = "t - n";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 2, 20, 30, 9, 0, zone), p);
    }

    @Test
    public void zonedDateTimeMinusPeriod() {
        context.put("t", ZonedDateTime.of(2020, 10, 2, 1, 30, 9, 0, zone));
        final String code = "t - 2.days";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 30, 1, 30, 9, 0, zone), p);
    }

    @Test
    public void zonedDateTimeMinusNumber() {
        context.put("t", ZonedDateTime.of(2020, 9, 3, 1, 30, 0, 0, zone));
        context.put("n", 5);
        final String code = "t - n";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 3, 1, 29, 55, 0, zone), p);
    }

    @Test
    public void zonedDateTimeMinusZonedDateTime() {
        final LocalDateTime dateTime = LocalDateTime.of(2020, 9, 8, 0, 0, 0, 0);
        context.put("p", ZonedDateTime.of(dateTime, ZoneId.of("Europe/Paris")));
        context.put("t", ZonedDateTime.of(dateTime, ZoneId.of("Africa/Tunis")));

        final Duration p = (Duration) eval("p - t", context);
        assertEquals(Duration.ofHours(-1), p);

        context.put("d", p);
        assertEquals(true, eval("~p == ~t + d", context));
    }

    @Test
    public void zonedDateTimeNext() {
        context.put("t", ZonedDateTime.of(2020, 9, 3, 1, 30, 59, 0, zone));
        final String code = "++t";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 3, 1, 31, 0, 0, zone), p);
    }

    @Test
    public void zonedDateTimePrevious() {
        context.put("t", ZonedDateTime.of(2020, 9, 3, 1, 30, 0, 0, zone));
        final String code = "--t";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(2020, 9, 3, 1, 29, 59, 0, zone), p);
    }

    @Test
    public void zonedDateTimeBitwiseNegate() {
        final Instant moment = Instant.now();
        context.put("t", moment.atZone(ZoneId.systemDefault()));
        final String code = "~t";
        final Instant p = (Instant) eval(code, context);
        assertEquals(moment, p);
    }

}
