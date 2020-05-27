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
 * Test cases for Year operations.
 */
@RunWith(JUnit4.class)
public class YearTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void yearPlusNumber() {
        context.put("t", Year.of(2020));
        context.put("b", 2);
        String code = "t + b";
        Object res = eval(code, context);
        assertEquals(Year.of(2022), res);
    }

    @Test
    public void yearMinusNumber() {
        context.put("t", Year.of(2020));
        context.put("b", 2);
        String code = "t - b";
        Object res = eval(code, context);
        assertEquals(Year.of(2018), res);
    }

    @Test
    public void yearMinusYear() {
        context.put("t", Year.of(2020));
        context.put("b", Year.of(2022));

        Object res = eval("t - b", context);
        assertEquals(Period.ofYears(-2), res);

        context.put("d", res);
        assertEquals(true, eval("t == b + d", context));
    }

    @Test
    public void yearNext() {
        context.put("t", Year.of(2020));
        String code = "++t";
        Object res = eval(code, context);
        assertEquals(Year.of(2021), res);
    }

    @Test
    public void yearPrevious() {
        context.put("t", Year.of(2020));
        String code = "--t";
        Object res = eval(code, context);
        assertEquals(Year.of(2019), res);
    }

    @Test
    public void yearCombineWithMonth() {
        context.put("t", Year.of(2020));
        context.put("b", Month.OCTOBER);
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(YearMonth.of(2020, 10), res);
    }

    @Test
    public void yearCombineWithMonthDay() {
        context.put("t", Year.of(2020));
        context.put("b", MonthDay.of(10, 1));
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(LocalDate.of(2020, 10, 1), res);
    }

}
