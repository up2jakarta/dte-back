package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for MonthDay operations.
 */
@RunWith(JUnit4.class)
public class MonthDayTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void monthDayCombineWithNumber() {
        context.put("t", MonthDay.of(10, 21));
        context.put("b", 2020);
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(LocalDate.of(2020, 10, 21), res);
    }

    @Test
    public void monthDayCombineWithYear() {
        context.put("t", MonthDay.of(10, 21));
        context.put("b", Year.of(2020));
        String code = "t | b";
        Object res = eval(code, context);
        assertEquals(LocalDate.of(2020, 10, 21), res);
    }

}
