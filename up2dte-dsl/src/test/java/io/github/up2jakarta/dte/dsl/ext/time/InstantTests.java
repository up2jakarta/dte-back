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
 * Test cases for Instant operations.
 */
@RunWith(JUnit4.class)
public class InstantTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneOffset offset = ZoneOffset.of("+01:30");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void instantPlusNumber() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset).toInstant());
        context.put("n", 9);
        final String code = "t + n";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 30, 9, 0, offset).toInstant(), p);
    }

    @Test
    public void instantPlusDuration() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 20, 30, 0, 0, offset).toInstant());
        context.put("n", Duration.ofHours(5));
        final String code = "t + n";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 4, 1, 30, 0, 0, offset).toInstant(), p);
    }

    @Test
    public void instantPlusPeriod() {
        context.put("t", OffsetDateTime.of(2020, 9, 30, 1, 30, 9, 0, offset).toInstant());
        final String code = "t + 2.days";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 10, 2, 1, 30, 9, 0, offset).toInstant(), p);
    }

    @Test
    public void instantMinusDuration() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 9, 0, offset).toInstant());
        context.put("n", Duration.ofHours(5));
        final String code = "t - n";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 2, 20, 30, 9, 0, offset).toInstant(), p);
    }

    @Test
    public void instantMinusPeriod() {
        context.put("t", OffsetDateTime.of(2020, 10, 2, 1, 30, 9, 0, offset).toInstant());
        final String code = "t - 2.days";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 30, 1, 30, 9, 0, offset).toInstant(), p);
    }

    @Test
    public void instantMinusNumber() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset).toInstant());
        context.put("n", 5);
        final String code = "t - n";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 29, 55, 0, offset).toInstant(), p);
    }

    @Test
    public void instantMinusInstant() {
        final LocalDateTime dateTime = LocalDateTime.of(2020, 9, 8, 23, 30, 9, 0);
        context.put("p", OffsetDateTime.of(dateTime, ZoneOffset.ofHours(2)).toInstant());
        context.put("t", OffsetDateTime.of(dateTime, ZoneOffset.ofHours(1)).toInstant());

        final Duration p = (Duration) eval("p - t", context);
        assertEquals(Duration.ofHours(-1), p);

        context.put("d", p);
        assertEquals(true, eval("p == t + d", context));
    }

    @Test
    public void instantNext() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 59, 0, offset).toInstant());
        final String code = "++t";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 31, 0, 0, offset).toInstant(), p);
    }

    @Test
    public void instantPrevious() {
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset).toInstant());
        final String code = "--t";
        final Instant p = (Instant) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 29, 59, 0, offset).toInstant(), p);
    }

    @Test
    public void instantCombineWithZoneId() {
        final ZoneId zone1 = ZoneId.of("Europe/Paris");
        final ZoneId zone2 = ZoneId.of("Africa/Tunis");
        final String code = "t | z";

        final LocalDateTime date = LocalDateTime.of(2020, 1, 3, 23, 30, 0, 0);
        context.put("t", ZonedDateTime.of(date, zone1).toInstant());

        context.put("z", zone1);
        ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(date, zone1), p);

        context.put("z", zone2);
        p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(date, zone2), p);
    }

    @Test
    public void instantCombineWithZoneOffset() {
        final ZoneOffset zone1 = ZoneOffset.ofHoursMinutes(1, 30);
        final ZoneOffset zone2 = ZoneOffset.ofHours(1);
        final String code = "t | z";
        final LocalDateTime date = LocalDateTime.of(2020, 1, 3, 10, 30, 0, 0);
        context.put("t", OffsetDateTime.of(date, zone1).toInstant());

        context.put("z", zone1);
        OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(date, zone1), p);

        context.put("z", zone2);
        p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(date, zone2).minusMinutes(30), p);
    }
}
