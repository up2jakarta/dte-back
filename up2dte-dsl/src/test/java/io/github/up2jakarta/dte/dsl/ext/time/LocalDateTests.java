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
 * Test cases for LocalDate operations.
 */
@RunWith(JUnit4.class)
public class LocalDateTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void localDatePlusPeriod() {

        context.put("t", LocalDate.of(1999, 1, 1));
        context.put("p", Period.of(1, 2, 3));
        final String code = "t + p";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(2000, 3, 4), p);
    }

    @Test
    public void localDatePlusNumber() {
        context.put("t", LocalDate.of(1999, 1, 1));
        context.put("n", 6940);
        final String code = "t + n";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(2018, 1, 1), p);
    }

    @Test
    public void localDateMinusLocalDate() {
        context.put("t1", LocalDate.of(1999, 1, 1));
        context.put("t2", LocalDate.of(2018, 6, 16));

        final Period p = (Period) eval("t2 - t1", context);
        assertEquals(Period.of(19, 5, 15), p);

        context.put("d", p);
        assertEquals(true, eval("t2 == t1 + d", context));
    }

    @Test
    public void localDateMinusPeriod() {

        context.put("t", LocalDate.of(2000, 3, 4));
        context.put("p", Period.of(1, 2, 3));
        final String code = "t - p";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(1999, 1, 1), p);
    }

    @Test
    public void localDateMinusNumber() {
        context.put("t", LocalDate.of(2018, 1, 2));
        context.put("n", 6940);
        final String code = "t - n";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(1999, 1, 2), p);
    }

    @Test
    public void localDateNext() {
        context.put("t", LocalDate.of(2018, 1, 2));
        final String code = "++t";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(2018, 1, 3), p);
    }

    @Test
    public void localDatePrevious() {
        context.put("t", LocalDate.of(2018, 1, 2));
        final String code = "--t";
        final LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(2018, 1, 1), p);
    }

    @Test
    public void localDateCombineWithLocalTime() {
        context.put("t1", LocalDate.of(2018, 1, 1));
        context.put("t2", LocalTime.of(10, 11, 12));
        final String code = "t1 | t2";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2018, 1, 1, 10, 11, 12), p);
    }

    @Test
    public void localDateCombineWithOffsetTime() {
        context.put("t1", LocalDate.of(2018, 1, 1));
        context.put("t2", OffsetTime.of(10, 11, 12, 13, ZoneOffset.of("+01:30")));
        final String code = "t1 | t2";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2018, 1, 1, 10, 11, 12, 13, ZoneOffset.of("+01:30")), p);
    }

    @Test
    public void firstDayOfNextMonth() {
        context.put("a", LocalDate.of(2013, 2, 21));
        final Object res = eval("a.firstDayOfNextMonth()", context);
        assertEquals(LocalDate.of(2013, 3, 1), res);
    }

    @Test
    public void firstDayOfNextYear() {
        context.put("a", LocalDate.of(2013, 2, 1));
        final Object res = eval("a.firstDayOfNextYear()", context);
        assertEquals(LocalDate.of(2014, 1, 1), res);
    }

    @Test
    public void firstDayOfMonth() {
        context.put("a", LocalDate.of(2013, 2, 2));
        final Object res = eval("a.firstDayOfMonth()", context);
        assertEquals(LocalDate.of(2013, 2, 1), res);
    }

    @Test
    public void firstDayOfYear() {
        context.put("a", LocalDate.of(2013, 2, 2));
        final Object res = eval("a.firstDayOfYear()", context);
        assertEquals(LocalDate.of(2013, 1, 1), res);
    }

    @Test
    public void lastDayOfYear() {
        context.put("a", LocalDate.of(2013, 2, 1));
        final Object res = eval("a.lastDayOfYear()", context);
        assertEquals(LocalDate.of(2013, 12, 31), res);
    }

    @Test
    public void lastDayOfMonth() {
        context.put("a", LocalDate.of(2013, 12, 1));
        final Object res = eval("a.lastDayOfMonth()", context);
        assertEquals(LocalDate.of(2013, 12, 31), res);
    }

    @Test
    public void lastInMonth() {
        context.put("a", LocalDate.of(2020, 9, 3));
        context.put("b", DayOfWeek.FRIDAY);
        final Object res = eval("a.lastInMonth(b)", context);
        assertEquals(LocalDate.of(2020, 9, 25), res);
    }

    @Test
    public void firstInMonth() {
        context.put("a", LocalDate.of(2020, 9, 2));
        context.put("b", DayOfWeek.FRIDAY);
        final Object res = eval("a.firstInMonth(b)", context);
        assertEquals(LocalDate.of(2020, 9, 4), res);
    }

    @Test
    public void next() {
        context.put("a", LocalDate.of(2020, 9, 2));
        context.put("b", DayOfWeek.FRIDAY);
        final Object res = eval("a.next(b)", context);
        assertEquals(LocalDate.of(2020, 9, 4), res);
    }

    @Test
    public void previous() {
        context.put("a", LocalDate.of(2020, 9, 2));
        context.put("b", DayOfWeek.FRIDAY);
        final Object res = eval("a.previous(b)", context);
        assertEquals(LocalDate.of(2020, 8, 28), res);
    }

    @Test
    public void nextOrSame() {
        context.put("a", LocalDate.of(2020, 9, 2));
        context.put("b", DayOfWeek.WEDNESDAY);
        final Object res = eval("a.nextOrSame(b)", context);
        assertEquals(LocalDate.of(2020, 9, 2), res);
    }

    @Test
    public void previousOrSame() {
        context.put("a", LocalDate.of(2020, 9, 2));
        context.put("b", DayOfWeek.WEDNESDAY);
        final Object res = eval("a.previousOrSame(b)", context);
        assertEquals(LocalDate.of(2020, 9, 2), res);
    }

}
