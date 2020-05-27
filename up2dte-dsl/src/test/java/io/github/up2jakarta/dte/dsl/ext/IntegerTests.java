package io.github.up2jakarta.dte.dsl.ext;

import org.junit.After;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.*;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for Number operations
 */
@RunWith(JUnit4.class)
public class IntegerTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void numberPlusLocalDate() {
        context.put("t", LocalDate.of(1999, 1, 1));
        context.put("n", 6940);
        String code = "n + t";
        LocalDate p = (LocalDate) eval(code, context);
        assertEquals(LocalDate.of(2018, 1, 1), p);
    }

    @Test
    public void numberToDuration() {
        Assert.assertEquals(Duration.ofHours(2), eval("2.hours", context));
        Assert.assertEquals(Duration.ofHours(1), eval("1.hour", context));

        Assert.assertEquals(Duration.ofMinutes(2), eval("2.minutes", context));
        Assert.assertEquals(Duration.ofMinutes(1), eval("1.minute", context));

        Assert.assertEquals(Duration.ofSeconds(2), eval("2.seconds", context));
        Assert.assertEquals(Duration.ofSeconds(1), eval("1.second", context));

        Assert.assertEquals(Duration.ofMillis(2), eval("2.millis", context));
        Assert.assertEquals(Duration.ofMillis(1), eval("1.milli", context));

        Assert.assertEquals(Duration.ofNanos(2), eval("2.nanos", context));
        Assert.assertEquals(Duration.ofNanos(1), eval("1.nano", context));
    }

    @Test
    public void numberPlusDuration() {
        context.put("t", Duration.ofSeconds(20));
        context.put("n", 20);
        final String code = "n + t";
        final Duration p = (Duration) eval(code, context);
        assertEquals(Duration.ofSeconds(40), p);
    }

    @Test
    public void numberMultiplyDuration() {
        context.put("t", Duration.ofSeconds(20));
        context.put("n", 2);
        final String code = "n * t";
        final Duration p = (Duration) eval(code, context);
        assertEquals(Duration.ofSeconds(40), p);
    }

    @Test
    public void numberToPeriod() {
        Assert.assertEquals(Period.ofYears(2), eval("2.years", context));
        Assert.assertEquals(Period.ofYears(1), eval("1.year", context));

        Assert.assertEquals(Period.ofMonths(2), eval("2.months", context));
        Assert.assertEquals(Period.ofMonths(1), eval("1.month", context));

        Assert.assertEquals(Period.ofWeeks(2), eval("2.weeks", context));
        Assert.assertEquals(Period.ofWeeks(1), eval("1.week", context));

        Assert.assertEquals(Period.ofDays(2), eval("2.days", context));
        Assert.assertEquals(Period.ofDays(1), eval("1.day", context));
    }

    @Test
    public void numberPlusPeriod() {
        context.put("t", Period.ofDays(20));
        context.put("n", 2);
        final String code = "n + t";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.ofDays(22), p);
    }

    @Test
    public void numberMultiplyPeriod() {
        context.put("t", Period.ofDays(20));
        context.put("n", 2);
        final String code = "n * t";
        final Period p = (Period) eval(code, context);
        assertEquals(Period.ofDays(40), p);
    }

    @Test
    public void numberPlusLocalTime() {
        context.put("t", LocalTime.of(20, 10, 0));
        context.put("n", 5);
        final String code = "n + t";
        final LocalTime p = (LocalTime) eval(code, context);
        assertEquals(LocalTime.of(20, 10, 5), p);
    }

    @Test
    public void numberPlusLocalDateTime() {
        context.put("t", LocalDateTime.of(2020, 1, 10, 20, 10, 0));
        context.put("n", 5);
        final String code = "n + t";
        final LocalDateTime p = (LocalDateTime) eval(code, context);
        assertEquals(LocalDateTime.of(2020, 1, 10, 20, 10, 5), p);
    }

    @Test
    public void numberPlusYearMonth() {
        context.put("t", YearMonth.of(2020, 1));
        context.put("n", 2);
        String code = "n + t";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2020, 3), res);
    }

    @Test
    public void numberPlusYear() {
        context.put("t", Year.of(2005));
        context.put("n", 15);
        final String code = "n + t";
        final Year p = (Year) eval(code, context);
        assertEquals(Year.of(2020), p);
    }

    @Test
    public void numberPlusMonth() {
        context.put("t", Month.FEBRUARY);
        context.put("n", 11);
        final String code = "n + t";
        final Month p = (Month) eval(code, context);
        assertEquals(Month.JANUARY, p);
    }

    @Test
    public void numberPlusDayOfWeek() {
        context.put("t", DayOfWeek.WEDNESDAY);
        context.put("n", 9);
        final String code = "n + t";
        final DayOfWeek p = (DayOfWeek) eval(code, context);
        assertEquals(DayOfWeek.FRIDAY, p);
    }

    @Test
    public void numberPlusOffsetTime() {
        final ZoneOffset offset = ZoneOffset.of("+01:30");
        context.put("t", OffsetTime.of(1, 30, 0, 0, offset));
        context.put("n", 9);
        final String code = "n + t";
        final OffsetTime p = (OffsetTime) eval(code, context);
        assertEquals(OffsetTime.of(1, 30, 9, 0, offset), p);
    }

    @Test
    public void numberPlusOffsetDateTime() {
        final ZoneOffset offset = ZoneOffset.of("+01:30");
        context.put("t", OffsetDateTime.of(2020, 9, 3, 1, 30, 0, 0, offset));
        context.put("n", 9);
        final String code = "n + t";
        final OffsetDateTime p = (OffsetDateTime) eval(code, context);
        assertEquals(OffsetDateTime.of(2020, 9, 3, 1, 30, 9, 0, offset), p);
    }

    @Test
    public void numberPlusZonedDateTime() {
        final ZoneId zone = ZoneId.of("Africa/Tunis");
        context.put("t", ZonedDateTime.of(LocalDateTime.of(2020, 1, 10, 20, 10, 0), zone));
        context.put("n", 9);
        final String code = "n + t";
        final ZonedDateTime p = (ZonedDateTime) eval(code, context);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2020, 1, 10, 20, 10, 9), zone), p);
    }

    @Test
    public void numberPlusInstant() {
        final ZoneId zone = ZoneId.of("Africa/Tunis");
        context.put("t", ZonedDateTime.of(LocalDateTime.of(2020, 1, 10, 20, 10, 0), zone).toInstant());
        context.put("n", 9);
        final String code = "n + t";
        final Instant p = (Instant) eval(code, context);
        assertEquals(ZonedDateTime.of(LocalDateTime.of(2020, 1, 10, 20, 10, 9), zone).toInstant(), p);
    }

    @Test
    public void numberCombineWithMonthDay() {
        context.put("t", MonthDay.of(10, 21));
        context.put("b", 2020);
        String code = "b | t";
        Object res = eval(code, context);
        assertEquals(LocalDate.of(2020, 10, 21), res);
    }

    @Test
    public void numberCombineWithMonth() {
        context.put("t", Month.JANUARY);
        context.put("b", 12);
        String code = "b | t";
        Object res = eval(code, context);
        assertEquals(MonthDay.of(1, 12), res);
    }

    @Test
    public void numberCombineWithYearMonth() {
        context.put("t", YearMonth.of(2020, 1));
        context.put("b", 21);
        String code = "b | t";
        Object res = eval(code, context);
        assertEquals(LocalDate.of(2020, 1, 21), res);
    }

}
