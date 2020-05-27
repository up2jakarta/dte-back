package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for ZoneOffset operations.
 */
@RunWith(JUnit4.class)
public class ZoneOffsetTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneOffset offset = ZoneOffset.of("+01:30");
    private final ZoneOffset negative = ZoneOffset.of("-01:30:30");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void shouldMinusZoneOffset() {
        context.put("t", ZoneOffset.of("+01:00"));
        context.put("z", offset);

        final Object res = eval("z - t", context);
        assertEquals(Duration.ofMinutes(-30), res);

        context.put("d", res);
        context.put("dt", LocalDateTime.of(2020, 9, 6, 17, 54));
        assertEquals(true, eval("~(dt | z) == ~((dt | t) + d)", context));
    }

    @Test
    public void shouldCombineWithInstant() {
        final ZoneOffset zone1 = ZoneOffset.ofHoursMinutes(1, 30);
        final ZoneOffset zone2 = ZoneOffset.ofHours(1);
        final String code = "z | t";
        final LocalDateTime date = LocalDateTime.of(2020, 1, 3, 10, 30, 0, 0);
        context.put("t", OffsetDateTime.of(date, zone1).toInstant());

        context.put("z", zone1);
        OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(date, zone1), p);

        context.put("z", zone2);
        p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(date, zone2).minusMinutes(30), p);
    }

    @Test
    public void shouldCombineWithLocalDateTime() {
        final LocalDateTime date = LocalDateTime.of(2020, 9, 6, 17, 54);
        context.put("t", date);
        context.put("z", offset);
        String code = "z | t";
        Object res = eval(code, context);
        assertEquals(OffsetDateTime.of(date, offset), res);
    }

    @Test
    public void shouldCombineWithLocalTime() {
        final LocalTime time = LocalTime.of(17, 54);
        context.put("t", time);
        context.put("z", offset);
        String code = "z | t";
        Object res = eval(code, context);
        assertEquals(OffsetTime.of(time, offset), res);
    }

    @Test
    public void shouldToTimeZone() {
        context.put("z", offset);
        String code = "z.toTimeZone()";
        Object res = eval(code, context);
        assertEquals(TimeZone.getTimeZone(offset), res);
    }

    @Test
    public void shouldGetHours() {
        context.put("z", offset);
        Object res = eval("z.hours", context);
        assertEquals(1, res);
    }

    @Test
    public void shouldGetMinutes() {
        context.put("z", offset);
        Object res = eval("z.minutes", context);
        assertEquals(30, res);
    }

    @Test
    public void shouldGetSeconds() {
        context.put("z", offset);
        Object res = eval("z.seconds", context);
        assertEquals(0, res);
    }

    @Test
    public void shouldGetHoursNegative() {
        context.put("z", negative);
        Object res = eval("z.hours", context);
        assertEquals(-1, res);
    }

    @Test
    public void shouldGetMinutesNegative() {
        context.put("z", negative);
        Object res = eval("z.minutes", context);
        assertEquals(-30, res);
    }

    @Test
    public void shouldGetSecondsNegative() {
        context.put("z", negative);
        Object res = eval("z.seconds", context);
        assertEquals(-30, res);
    }

}
