package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for Month operations.
 */
@RunWith(JUnit4.class)
public class MonthTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void monthPlusNumber() {
        context.put("t", Month.JANUARY);
        context.put("b", 2);
        String code = "t + b";
        Object res = eval(code, context);
        assertEquals(Month.MARCH, res);
    }

    @Test
    public void monthMinusNumber() {
        context.put("t", Month.JANUARY);
        context.put("b", 2);
        String code = "t - b";
        Object res = eval(code, context);
        assertEquals(Month.NOVEMBER, res);
    }

    @Test
    public void monthNext() {
        context.put("t", Month.JANUARY);
        String code = "++t";
        Object res = eval(code, context);
        assertEquals(Month.FEBRUARY, res);
    }

    @Test
    public void monthPrevious() {
        context.put("t", Month.JANUARY);
        String code = "--t";
        Object res = eval(code, context);
        assertEquals(Month.DECEMBER, res);
    }

    @Test
    public void monthCombineWithNumber() {
        context.put("t", Month.JANUARY);
        context.put("b", 12);
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(MonthDay.of(1, 12), res);
    }

    @Test
    public void monthCombineWithYear() {
        context.put("t", Month.JANUARY);
        context.put("b", Year.of(2020));
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2020, 1), res);
    }

}
