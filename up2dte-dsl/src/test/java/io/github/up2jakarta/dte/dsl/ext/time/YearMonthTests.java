package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for YearMonth operations.
 */
@RunWith(JUnit4.class)
public class YearMonthTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void yearMonthPlusNumber() {
        context.put("t", YearMonth.of(2020, 1));
        context.put("b", 2);
        String code = "t + b";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2020, 3), res);
    }

    @Test
    public void yearMonthMinusNumber() {
        context.put("t", YearMonth.of(2020, 3));
        context.put("b", 2);
        String code = "t - b";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2020, 1), res);
    }

    @Test
    public void yearMonthMinusYearMonth() {
        context.put("t", YearMonth.of(2022, 3));
        context.put("b", YearMonth.of(2020, 5));

        Object res = eval("t - b", context);
        assertEquals(Period.ofYears(1).withMonths(10), res);

        context.put("d", res);
        assertEquals(true, eval("t == b + d", context));
    }

    @Test
    public void yearMonthNext() {
        context.put("t", YearMonth.of(2019, 12));
        String code = "++t";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2020, 1), res);
    }

    @Test
    public void yearMonthPrevious() {
        context.put("t", YearMonth.of(2020, 1));
        String code = "--t";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2019, 12), res);
    }

    @Test
    public void yearMonthCombineWithNumber() {
        context.put("t", YearMonth.of(2020, 1));
        context.put("b", 21);
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(LocalDate.of(2020, 1, 21), res);
    }

}
