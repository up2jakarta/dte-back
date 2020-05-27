package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for Java.Time parsers.
 */
@RunWith(JUnit4.class)
public class StaticParserTests {

    private final Map<String, Object> context = new HashMap<>();

    @Test
    public void shouldParseLocalDateTime() {
        final String code = "LocalDateTime.parse('2011-12-03T10:15:30.000000123')";
        final Object res = eval(code, context);
        assertEquals(LocalDateTime.of(2011, 12, 3, 10, 15, 30, 123), res);
    }

    @Test
    public void shouldParseLocalDate() {
        final String code = "LocalDate.parse('2011-12-03')";
        final Object res = eval(code, context);
        assertEquals(LocalDate.of(2011, 12, 3), res);
    }

    @Test
    public void shouldParseLocalTime() {
        final String code = "LocalTime.parse('10:15:30')";
        final Object res = eval(code, context);
        assertEquals(LocalTime.of(10, 15, 30), res);
    }

    @Test
    public void shouldParseOffsetDateTime() {
        final String code = "OffsetDateTime.parse('2011-12-03T10:15:30+01:00')";
        final Object res = eval(code, context);
        assertEquals(OffsetDateTime.of(2011, 12, 3, 10, 15, 30, 0, ZoneOffset.ofHours(1)), res);
    }

    @Test
    public void shouldParseOffsetTime() {
        final String code = "OffsetTime.parse('10:15:30+01:00')";
        final Object res = eval(code, context);
        assertEquals(OffsetTime.of(10, 15, 30, 0, ZoneOffset.ofHours(1)), res);
    }

    @Test
    public void shouldParseZonedDateTime() {
        final String code = "ZonedDateTime.parse('2011-12-03T10:15:30+01:00[Europe/Paris]')";
        final Object res = eval(code, context);
        assertEquals(ZonedDateTime.of(2011, 12, 3, 10, 15, 30, 0, ZoneId.of("Europe/Paris")), res);
    }

    @Test
    public void shouldParseYear() {
        final String code = "Year.parse('2011')";
        final Object res = eval(code, context);
        assertEquals(Year.of(2011), res);
    }

    @Test
    public void shouldParseYearMonth() {
        final String code = "YearMonth.parse('2011-12')";
        final Object res = eval(code, context);
        assertEquals(YearMonth.of(2011, 12), res);
    }

    @Test
    public void shouldParseMonthDay() {
        final String code = "MonthDay.parse('12-03')";
        final Object res = eval(code, context);
        assertEquals(MonthDay.of(12, 3), res);
    }

}
