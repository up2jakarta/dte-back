package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.*;
import java.time.format.TextStyle;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for ZoneId operations.
 */
@RunWith(JUnit4.class)
public class ZoneIdTests {

    private final Map<String, Object> context = new HashMap<>();
    private final ZoneId zone = ZoneId.of("Africa/Tunis");

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void zoneIdCombineWithInstant() {
        final ZoneId zone1 = ZoneId.of("Europe/Paris");
        final ZoneId zone2 = ZoneId.of("Africa/Tunis");
        final String code = "z | t";

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
    public void zoneIdCombineWithOffsetDateTime() {
        final ZoneOffset offset = ZoneOffset.of("+01:30");
        final String code = "z | t";
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
    public void zoneIdCombineWithLocalDateTime() {
        final LocalDateTime date = LocalDateTime.of(2020, 9, 6, 17, 54);
        context.put("t", date);
        context.put("z", zone);
        String code = "z | t";
        Object res = eval(code, context);
        assertEquals(ZonedDateTime.of(date, zone), res);
    }

    @Test
    public void zoneIdToTimeZone() {
        context.put("z", zone);
        String code = "z.toTimeZone()";
        Object res = eval(code, context);
        assertEquals(TimeZone.getTimeZone(zone), res);
    }

    @Test
    public void zoneIdGetFullName() {
        context.put("z", zone);
        String code = "z.fullName";
        Object res = eval(code, context);
        assertEquals(zone.getDisplayName(TextStyle.FULL, Locale.getDefault()), res);
    }

    @Test
    public void zoneIdGetShortName() {
        context.put("z", zone);
        String code = "z.shortName";
        Object res = eval(code, context);
        assertEquals(zone.getDisplayName(TextStyle.SHORT, Locale.getDefault()), res);
    }

    @Test
    public void zoneIdGetFullNameWithLocale() {
        context.put("l", Locale.ENGLISH);
        context.put("z", zone);
        String code = "z.fullName(l)";
        Object res = eval(code, context);
        assertEquals("Central European Time", res);
    }

    @Test
    public void zoneIdGetShortNameWithLocale() {
        context.put("l", Locale.FRANCE);
        context.put("z", zone);
        String code = "z.shortName(l)";
        Object res = eval(code, context);
        assertEquals("CET", res);
    }

    @Test
    public void zoneIdGetOffset() {
        context.put("z", zone);
        String code = "z.offset";
        Object res = eval(code, context);
        assertEquals(zone.getRules().getOffset(Instant.now()), res);
    }

    @Test
    public void zoneIdGetOffsetWithInstant() {
        final Instant moment = LocalDateTime.of(2020, 9, 6, 17, 54).toInstant(ZoneOffset.UTC);
        context.put("i", moment);
        context.put("z", zone);
        String code = "z.offset(i)";
        Object res = eval(code, context);
        assertEquals(zone.getRules().getOffset(moment), res);
    }

}
