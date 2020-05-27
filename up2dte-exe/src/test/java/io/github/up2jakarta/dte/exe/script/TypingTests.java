package io.github.up2jakarta.dte.exe.script;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import io.github.up2jakarta.dte.dsl.BaseScript;

import java.time.Month;
import java.time.MonthDay;
import java.time.OffsetTime;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import static io.github.up2jakarta.dte.exe.engine.ScriptCompiler.compile;

/**
 * Test cases for formula validation
 */
@RunWith(JUnit4.class)
public class TypingTests {

    @Test(expected = CompilationException.class)
    public void exceptionMessage() {
        final String script = "b = x()";
        compile(BaseScript.class, script, Collections.emptyMap());
    }

    @Test
    public void combineOperator() {
        // Given
        final String script = "a = 1 | t";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.singletonMap("t", Month.class));

        // then
        Assert.assertEquals(MonthDay.class, typing.get("a"));
    }

    @Test
    public void legacyCombineOperator() {
        // Given
        final Map<String, Class<?>> types = new HashMap<>();
        {
            types.put("a", Boolean.class);
            types.put("b", Boolean.class);
        }
        final String script = "a = a | b";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, types);

        // then
        Assert.assertEquals(Boolean.class, typing.get("a"));
    }

    @Test
    public void localVariable() {
        // Given
        final String script = "def a = 100" +
                "\nb = a";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("b"));
    }

    @Test
    public void shouldZonedDateTime() {
        // Given
        final String script = "r = 10.months + t";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.singletonMap("t", ZonedDateTime.class));

        // then
        Assert.assertEquals(ZonedDateTime.class, typing.get("r"));
    }

    @Test
    public void shouldOffsetTime() {
        // Given
        final String script = "r = 1.hour + t";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.singletonMap("t", OffsetTime.class));

        // then
        Assert.assertEquals(OffsetTime.class, typing.get("r"));
    }

    @Test
    public void localFunction1() {
        // Given
        final String script = "def boolean negate(boolean a) { return !a}\n" +
                "b = negate(true)";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Boolean.class, typing.get("b"));
    }

    @Test
    public void localFunctions2() {
        // Given
        final String script = "def double sum(Double a, Double b) {\n" +
                "def x = (a != null) ? a : 0\n" +
                "def y = (b != null) ? b : 0\n" +
                "return x + y \n" +
                "}\n" +
                "s = sum(1, 9)";
        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(1, typing.size());
        Assert.assertEquals(Double.class, typing.get("s"));
    }

    @Test
    public void localFunctions() {
        // Given
        final String script = "def int x() { return 100}\n" +
                "a = has('vv')\n" +
                "b = x()";
        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Boolean.class, typing.get("a"));
        Assert.assertEquals(Integer.class, typing.get("b"));
    }

    @Test
    public void varArgsFunction() {
        // Given
        final String script = "def int sum(int... t) {\n" +
                "def int s = 0\n" +
                "for (v in t) {\ns += v\n}\n" +
                "return s\n" +
                "}\n" +
                "a = sum(1, 10, 100, 1000)";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("a"));
    }

    @Test
    public void ternaryExpression() {
        // Given
        final String script = "def Double a = null\nx = ( a != null) ? a : 0";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(1, typing.size());
        Assert.assertEquals(Double.class, typing.get("x"));
    }

    @Test
    public void ternaryExpression2() {
        // Given
        final String script = "def Integer a = null\nx = ( a != null) ? a : 0";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(1, typing.size());
        Assert.assertEquals(Integer.class, typing.get("x"));
    }

    @Test
    public void ternaryExpression3() {
        // Given
        final String script = "def List a = []\n" +
                "x = ( a != null) ? a : new HashSet()";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());
        // then
        Assert.assertEquals(1, typing.size());
        Assert.assertTrue(Collection.class.isAssignableFrom(typing.get("x")));
    }

    @Test
    public void genericMethod() {
        // Given
        final String script = "a = nvl(1, 10, 100, 1000)\n" +
                "b = nvl(null, null, \"AAB\")\n" +
                "c = nvl(null, 1.1F)\n" +
                "d = nvl(null, null)\n" +
                "e = nvl(1, 1.1F)\n";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("a"));
        Assert.assertEquals(String.class, typing.get("b"));
        Assert.assertEquals(Float.class, typing.get("c"));
        Assert.assertNull(typing.get("d"));
        Assert.assertEquals(Number.class, typing.get("e"));
    }

    @Test
    public void internalGenericMethodWithVarargsV1() {
        // Given
        final String script = "def <T> T test_nvl(T v1, T v2, T... values) {\n" +
                "    if (v1 != null) {\n" +
                "        return v1\n" +
                "    }\n" +
                "    if (v2 != null) {\n" +
                "        return v2\n" +
                "    }\n" +
                "    for (value in values) {\n" +
                "        if (value != null) return value\n" +
                "    }\n" +
                "    return null" +
                "}\n" +
                "a = test_nvl(1, 10, 100, 1000)\n" +
                "b = test_nvl(null, null, \"AAB\")\n" +
                "c = test_nvl(null, 1.1F)\n" +
                "d = test_nvl(null, null)\n" +
                "e = test_nvl(1, 1.1F)\n";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("a"));
        Assert.assertEquals(String.class, typing.get("b"));
        Assert.assertEquals(Float.class, typing.get("c"));
        Assert.assertNull(typing.get("d"));
        Assert.assertEquals(Number.class, typing.get("e"));
    }

    @Test
    public void internalGenericMethodWithVarargsV2() {
        // Given
        final String script = "def <T> T test_nvl(T... values) {\n" +
                "    for (value in values) {\n" +
                "        if (value != null) return value\n" +
                "    }\n" +
                "    return null" +
                "}\n" +
                "a = test_nvl(1, 10, 100, 1000)\n" +
                "b = test_nvl(null, null, \"AAB\")\n" +
                "c = test_nvl(null, 1.1F)\n" +
                "d = test_nvl(null, null)\n" +
                "e = test_nvl(1, 1.1F)\n" +
                "f = test_nvl(1, null, \"AAB\")";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("a"));
        Assert.assertEquals(String.class, typing.get("b"));
        Assert.assertEquals(Float.class, typing.get("c"));
        Assert.assertNull(typing.get("d"));
        Assert.assertEquals(Number.class, typing.get("e"));
        Assert.assertEquals(Object.class, typing.get("f"));
    }

    @Test
    public void internalGenericMethodWithVarargsV3() {
        // Given
        final String script = "def <T> T test_nvl(T[] tab, T... values) {\n" +
                "    for (value in tab) {\n" +
                "        if (value != null) return value\n" +
                "    }\n" +
                "    for (value in tab) {\n" +
                "        if (value != null) return value\n" +
                "    }\n" +
                "    return null" +
                "}\n" +
                "a = test_nvl([1, 10] as Integer[], 0, null)\n" +
                "b = test_nvl([1, 10.1F] as Float[], 0F, null)\n" +
                "c = test_nvl([\"B\", null] as String[], null, \"\")";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("a"));
        Assert.assertEquals(Float.class, typing.get("b"));
        Assert.assertEquals(String.class, typing.get("c"));
    }

    @Test
    public void shouldMatchesWithDefaultValues() {
        // Given
        final String script = "def <T> T test_nvl(T v1, T v2 = null) {\n" +
                "    return (v1 != null) ? v1 : v2\n" +
                "}\n" +
                "def <T> T test_nvl(T v1, T v2, T v3, T v4 = null) {\n" +
                "    return (v1 != null) ? v1 : v2\n" +
                "}\n" +
                "a = test_nvl(1)\n" +
                "b = test_nvl(1.1F)\n" +
                "c = test_nvl(\"B\")\n" +
                "d = test_nvl(1L, 1L, 2L)\n";

        // when
        final Map<String, Class<?>> typing = compile(BaseScript.class, script, Collections.emptyMap());

        // then
        Assert.assertEquals(Integer.class, typing.get("a"));
        Assert.assertEquals(Float.class, typing.get("b"));
        Assert.assertEquals(String.class, typing.get("c"));
        Assert.assertEquals(Long.class, typing.get("d"));
    }

}
