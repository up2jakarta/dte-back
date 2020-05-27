package io.github.up2jakarta.dte.dsl.ext.time;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.time.DayOfWeek;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.*;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for DayOfWeek operations.
 */
@RunWith(JUnit4.class)
public class DayOfWeekTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void dayOfWeekPlusNumber() {
        context.put("t", DayOfWeek.MONDAY);
        context.put("b", 2);
        String code = "t + b";
        Object res = eval(code, context);
        assertEquals(DayOfWeek.WEDNESDAY, res);
    }

    @Test
    public void dayOfWeekMinusNumber() {
        context.put("t", DayOfWeek.MONDAY);
        context.put("b", 9);
        String code = "t - b";
        Object res = eval(code, context);
        assertEquals(DayOfWeek.SATURDAY, res);
    }

    @Test
    public void dayOfWeekNext() {
        context.put("t", DayOfWeek.MONDAY);
        String code = "++t";
        Object res = eval(code, context);
        assertEquals(DayOfWeek.TUESDAY, res);
    }

    @Test
    public void dayOfWeekPrevious() {
        context.put("t", DayOfWeek.MONDAY);
        String code = "--t";
        Object res = eval(code, context);
        assertEquals(DayOfWeek.SUNDAY, res);
    }

    @Test
    public void isWeekend() {
        assertFalse(eval("DayOfWeek.MONDAY.weekend", context, Boolean.class));
        assertTrue(eval("DayOfWeek.SATURDAY.weekend", context, Boolean.class));
        assertTrue(eval("DayOfWeek.SUNDAY.weekend", context, Boolean.class));
    }

    @Test
    public void isWeekday() {
        assertTrue(eval("DayOfWeek.MONDAY.weekday", context, Boolean.class));
        assertFalse(eval("DayOfWeek.SATURDAY.weekday", context, Boolean.class));
        assertFalse(eval("DayOfWeek.SUNDAY.weekday", context, Boolean.class));
    }

}
