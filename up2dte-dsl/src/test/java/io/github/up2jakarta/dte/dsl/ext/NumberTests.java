package io.github.up2jakarta.dte.dsl.ext;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static io.github.up2jakarta.dte.dsl.Shell.eval;

/**
 * Test cases for Number operations
 */
@RunWith(JUnit4.class)
public class NumberTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void shouldRoundFloat() {
        context.put("a", 7.123458F);
        String code = "a.round(2)";
        Object res = eval(code, context);
        assertEquals(new BigDecimal("7.12"), res);
    }

    @Test
    public void shouldRoundDouble() {
        context.put("a", 7.1234580000000000000000001D);
        String code = "a.round(5)";
        Object res = eval(code, context);
        assertEquals(new BigDecimal("7.12345"), res);
    }

    @Test
    public void shouldRoundDecimal() {
        context.put("a", new BigDecimal("7.1234500014712599632114788555555555551"));
        String code = "a.round(30)";
        Object res = eval(code, context);
        assertEquals(new BigDecimal("7.123450001471259963211478855555"), res);
    }

    /* **********************************************  standards ********************************************** */
    @Test
    public void numericPlusOperator() {
        context.put("a", 1);
        context.put("b", 2);
        String code = "a + b";
        Object res = eval(code, context);
        assertEquals(3, res);
    }

    @Test
    public void numericMinusOperator() {
        context.put("a", 1);
        context.put("b", 0);
        String code = "a - b";
        Object res = eval(code, context);
        assertEquals(1, res);
    }

    @Test
    public void intMultiplyInt() {
        context.put("a", 1);
        context.put("b", 2);
        String code = "a * b";
        Object res = eval(code, context);
        assertEquals(2, res);
    }

    @Test
    public void intMultiplyByFloat() {
        context.put("a", 10);
        context.put("b", 20F);
        String code = "a * b";
        Object res = eval(code, context);
        assertEquals(200D, res);
    }

    @Test
    public void numericDivOperator() {
        context.put("a", 5);
        context.put("b", 2);
        String code = "a / b";
        Object res = eval(code, context);
        assertEquals(BigDecimal.valueOf(2.5), res);
    }

    @Test
    public void numericModOperator() {
        context.put("a", 5);
        context.put("b", 2);
        String code = "a % b";
        Object res = eval(code, context);
        assertEquals(1, res);
    }

    @Test
    public void numericNextOperator() {
        context.put("a", 1);
        Object res = eval("a++", context);
        assertEquals(1, res);
        assertEquals(context.get("a"), 2);

        res = eval("++a", context);
        assertEquals(3, res);
        assertEquals(context.get("a"), 3);
    }

    @Test
    public void compareTo0Operator() {
        context.put("a", 1);
        context.put("b", 1);
        Object res = eval("a <=> b", context);
        assertEquals(0, res);
    }

    @Test
    public void compareToP1Operator() {
        context.put("a", 1);
        context.put("b", 2);
        Object res = eval("a <=> b", context);
        assertEquals(-1, res);
    }

    @Test
    public void compareToN1Operator() {
        context.put("a", 3);
        context.put("b", 2);
        Object res = eval("a <=> b", context);
        assertEquals(1, res);
    }

    @Test
    public void superiorOperator() {
        context.put("a", 3);
        context.put("b", 2);
        Object res = eval("a > b", context);
        assertEquals(true, res);
    }

    @Test
    public void inferiorOperator() {
        context.put("a", 3);
        context.put("b", 2);
        Object res = eval("a < b", context);
        assertEquals(false, res);
    }

    @Test
    public void superiorOrEqualsOperator() {
        context.put("a", 3);
        context.put("b", 2);
        Object res = eval("a >= b", context);
        assertEquals(true, res);
    }

    @Test
    public void inferiorOrEqualsOperator() {
        context.put("a", 3);
        context.put("b", 3);
        Object res = eval("a <= b", context);
        assertEquals(true, res);
    }
}
