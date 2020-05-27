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
 * Test cases for Period operations.
 */
@RunWith(JUnit4.class)
public class PeriodTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void periodPlusOffsetDateTime() {
        final ZoneOffset offset = ZoneOffset.ofHours(1);
        context.put("d", Period.ofDays(10));
        context.put("t", OffsetDateTime.of(1999, 1, 1, 1, 1, 1, 0, offset));
        final String code = "d + t";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(1999, 1, 11, 1, 1, 1, 0, offset), p);
    }

    @Test
    public void periodPlusZonedDateTime() {
        final ZoneId zone = ZoneId.ofOffset("UTC", ZoneOffset.ofHours(1));
        context.put("d", Period.ofDays(35));
        context.put("t", ZonedDateTime.of(1999, 1, 1, 1, 1, 1, 0, zone));
        final String code = "d + t";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(1999, 2, 5, 1, 1, 1, 0, zone), p);
    }

    @Test
    public void periodPlusLocalDateTime() {
        context.put("p", Period.of(1, 2, 3));
        context.put("d", LocalDateTime.of(1999, 1, 1, 1, 1, 1));
        final String code = "p + d";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2000, 3, 4, 1, 1, 1), p);
    }

    @Test
    public void periodPlusLocalDate() {
        context.put("p", Period.of(1, 2, 3));
        context.put("d", LocalDate.of(1999, 1, 1));
        final String code = "p + d";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(2000, 3, 4), p);
    }

    @Test
    public void periodPlusNumber() {
        context.put("p", Period.ofMonths(1));
        context.put("n", 23);
        final String code = "p + n";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.of(0, 1, 23), p);
    }

    @Test
    public void periodMinusNumber() {
        context.put("p", Period.ofMonths(1));
        context.put("n", 23);
        final String code = "p -= n";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.of(0, 1, -23), p);
    }

    @Test
    public void periodNext() {
        context.put("p", Period.ofMonths(1));
        final String code = "++p";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.of(0, 1, 1), p);
    }

    @Test
    public void periodPrevious() {
        context.put("p", Period.ofMonths(1));
        final String code = "--p";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.of(0, 1, -1), p);
    }

    @Test
    public void periodNegative() {
        final String code = "-p";

        context.put("p", Period.of(1, 2, 3));
        Period p = (Period) eval(code, context);
        assertEquals(Period.of(-1, -2, -3), p);

        context.put("p", Period.of(-1, -2, -3));
        p = (Period) eval(code, context);
        assertEquals(Period.of(1, 2, 3), p);
    }

    @Test
    public void periodPositive() {
        final String code = "+p";

        context.put("p", Period.of(1, 2, 3));
        Period p = (Period) eval(code, context);
        assertEquals(Period.of(1, 2, 3), p);

        context.put("p", Period.of(-1, -2, -3));
        p = (Period) eval(code, context);
        assertEquals(Period.of(1, 2, 3), p);
    }

    @Test
    public void periodMultiplyNumber() {
        context.put("p", Period.of(1, 2, 4));
        context.put("n", 2);
        final String code = "p *= n";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.of(2, 4, 8), p);
    }

    @Test
    public void periodAgo() {
        final String code = "1.day.ago";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.now().minusDays(1), p);
    }

    @Test
    public void periodLater() {
        final String code = "1.week.later";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.now().plusDays(7), p);
    }

    @Test
    public void periodRightShift() {
        long d = (Long) eval("1.month >> LocalDate.of(2020, 2, 1)", context);
        assertEquals(29, d);

        d = (Long) eval("1.month >> LocalDate.of(2020, 1, 1)", context);
        assertEquals(31, d);

        d = (Long) eval("1.day >> LocalDate.of(2020, 1, 1)", context);
        assertEquals(1, d);

        d = (Long) eval("1.year >> LocalDate.of(2020, 1, 1)", context);
        assertEquals(366, d);
    }

    @Test
    public void periodLeftShift() {
        long d = (Long) eval("1.month << LocalDate.of(2020, 3, 1)", context);
        assertEquals(29, d);

        d = (Long) eval("1.month << LocalDate.of(2020, 2, 1)", context);
        assertEquals(31, d);

        d = (Long) eval("1.day << LocalDate.of(2020, 2, 1)", context);
        assertEquals(1, d);

        d = (Long) eval("1.year << LocalDate.of(2020, 2, 1)", context);
        assertEquals(365, d);
    }

}
