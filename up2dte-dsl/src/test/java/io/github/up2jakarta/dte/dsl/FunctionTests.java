package io.github.up2jakarta.dte.dsl;

import org.junit.After;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test cases for base script functions.
 */
@RunWith(JUnit4.class)
public class FunctionTests {

    private final Map<String, Object> context = new HashMap<>();

    @After
    public void tearDown() {
        context.clear();
    }

    @Test
    public void isEmptyObjectFunction() {
        String code = "empty(var)";
        context.put("var", null);
        Object res = Shell.eval(code, context);
        assertEquals(true, res);

        context.put("var", new Object());
        res = Shell.eval(code, context);
        assertEquals(false, res);
    }

    @Test
    public void isEmptyStringFunction() {
        String code = "empty(var)";
        context.put("var", "");
        Object res = Shell.eval(code, context);
        assertEquals(true, res);

        context.put("var", " ");
        res = Shell.eval(code, context);
        assertEquals(false, res);
    }

    @Test
    public void isEmptyArrayFunction() {
        String code = "empty(var)";
        context.put("var", new int[]{});
        Object res = Shell.eval(code, context);
        assertEquals(true, res);

        context.put("var", new String[]{""});
        res = Shell.eval(code, context);
        assertEquals(false, res);
    }

    @Test
    public void isEmptyCollectionFunction() {
        String code = "empty(var)";
        context.put("var", Collections.emptyList());
        Object res = Shell.eval(code, context);
        assertEquals(true, res);

        context.put("var", Collections.singleton(""));
        res = Shell.eval(code, context);
        assertEquals(false, res);
    }

    @Test
    public void isEmptyMapFunction() {
        String code = "empty(var)";
        context.put("var", Collections.emptyMap());
        Object res = Shell.eval(code, context);
        assertEquals(true, res);

        context.put("var", Collections.singletonMap(1, ""));
        res = Shell.eval(code, context);
        assertEquals(false, res);
    }

    @Test
    public void hasFunction() {
        String code = "has('var')";
        Object res = Shell.eval(code, context);
        assertEquals(false, res);

        context.put("var", 1);
        res = Shell.eval(code, context);
        assertEquals(true, res);
    }

    @Test
    public void nvlTwoArgumentFunction() {
        Object result = Shell.eval("nvl(null, 1L)", context);
        assertEquals(1L, result);
    }

    @Test
    public void nvlThreeArgumentFunction() {
        Object result = Shell.eval("nvl(null, null, 1L)", context);
        assertEquals(1L, result);
    }

    @Test
    public void nvlTwoNullArgumentFunction() {
        context.put("a", 1 / 11f);
        Object result = Shell.eval("nvl(null, null)", context);
        assertNull(result);
    }
}
