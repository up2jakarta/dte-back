package io.github.up2jakarta.dte.exe.engine;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import io.github.up2jakarta.dte.dsl.BaseScript;
import io.github.up2jakarta.dte.exe.StaticEngine;
import io.github.up2jakarta.dte.exe.script.CompilationException;

import java.time.LocalDate;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class ValidatorTests {

    @Test(expected = CompilationException.class)
    public void shouldHasFunctionFails() {
        StaticEngine.compile(BaseScript.class, "has()", Collections.emptyMap());
    }

    @Test(expected = CompilationException.class)
    public void invalidMethodArgs() {
        StaticEngine.compile(BaseScript.class, "nvl(a)", Collections.emptyMap());
    }

    @Test(expected = CompilationException.class)
    public void missingVariablesException() {
        StaticEngine.compile(BaseScript.class, "a + b", Collections.emptyMap());
    }

    @Test
    public void validExpression() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", Integer.class);
        types.put("b", Number.class);
        //then
        StaticEngine.compile(BaseScript.class, "a * b", types);
    }

    @Test
    public void validComplexExpression() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", Boolean.class);
        //then
        StaticEngine.compile(BaseScript.class, "has('a') && a || (2 * 5 / 10) > (100 - 1)", types);
    }

    @Test
    public void existingFunction() {
        StaticEngine.compile(BaseScript.class, "has(\"foo\")", Collections.emptyMap());
    }

    @Test(expected = CompilationException.class)
    public void notExistingFunction() {
        StaticEngine.compile(BaseScript.class, "notExisting(\"foo\")", Collections.emptyMap());
    }

    @Test(expected = CompilationException.class)
    public void wrongNumberOfArgumentsFunction() {
        StaticEngine.compile(BaseScript.class, "has(0, 1)", Collections.emptyMap());
    }

    @Test(expected = CompilationException.class)
    public void wrongExpression() {
        StaticEngine.compile(BaseScript.class, "a /+", Collections.singletonMap("a", int.class));
    }

    @Test(expected = CompilationException.class)
    public void wrongTypeOfArgumentFunction() {
        StaticEngine.compile(BaseScript.class, "has(1)", Collections.emptyMap());
    }

    @Test
    public void firstDayOfNextMonthFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", LocalDate.class);
        //then
        StaticEngine.compile(BaseScript.class, "a.firstDayOfNextMonth()", types);
    }

    @Test
    public void lastDayOfYearFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", LocalDate.class);
        //then
        StaticEngine.compile(BaseScript.class, "a.lastDayOfYear()", types);
    }

    @Test
    public void firstDayOfNextYearFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", LocalDate.class);
        //then
        StaticEngine.compile(BaseScript.class, "a.firstDayOfNextYear()", types);
    }

    @Test
    public void lastDayOfMonthFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", LocalDate.class);
        //then
        StaticEngine.compile(BaseScript.class, "a.lastDayOfMonth()", types);
    }

    @Test
    public void isEmptyFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", String.class);
        //then
        StaticEngine.compile(BaseScript.class, "empty(a)", types);
    }

    @Test
    public void requireFunctionWrongArg1() {
        //GIVEN
        final String msg = "Cannot find matching method Script#require(int, int)";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "require(1, 2)", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void requireFunctionWrongArg2() {
        //GIVEN
        final String msg = "Cannot find matching method Script#require(java.lang.String, boolean)";
        //WHEN
        CompilationException error = null;
        try {
            StaticEngine.compile(BaseScript.class, "require(\"a\", true)", Collections.emptyMap());
        } catch (CompilationException ex) {
            error = ex;
        }

        // then
        Assertions.assertThat(error)
                .isNotNull()
                .matches(e -> e.getMessage().contains(msg));
    }

    @Test
    public void hasFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", String.class);
        //then
        StaticEngine.compile(BaseScript.class, "has(a)", types);
    }

    @Test
    public void roundFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", Double.class);
        //then
        StaticEngine.compile(BaseScript.class, "a.round(2)", types);
    }

    @Test(expected = CompilationException.class)
    public void nvlNoArgumentFunction() {
        StaticEngine.compile(BaseScript.class, "nvl()", Collections.emptyMap());
    }

    @Test(expected = CompilationException.class)
    public void nvlOneArgumentFunction() {
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", Integer.class);
        //THEN
        StaticEngine.compile(BaseScript.class, "nvl(a)", types);
    }

    @Test
    public void nvlTwoArgumentFunction() {
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", Integer.class);
        types.put("b", Integer.class);
        //THEN
        StaticEngine.compile(BaseScript.class, "nvl(a, b)", types);
    }

    @Test
    public void nvlThreeArgumentFunction() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", Integer.class);
        types.put("b", Integer.class);
        //THEN
        StaticEngine.compile(BaseScript.class, "nvl(a, b, 1)", types);
    }

    @Test
    public void nvlPropertyExpressionArgument() {
        //Given
        final Map<String, Class<?>> types = new HashMap<>();
        types.put("a", long.class);
        types.put("b", long.class);
        //THEN
        StaticEngine.compile(BaseScript.class, "nvl(a, b, Long.MAX_VALUE, new Date().getTime())", types);
    }

    @Test(expected = CompilationException.class)
    public void syntaxErrorMessage() {
        //Given
        final String script = "else";
        //THEN
        StaticEngine.compile(BaseScript.class, script, Collections.emptyMap());
    }
}
