package io.github.up2jakarta.dte.exe.script;

import org.junit.Test;

import static org.junit.Assert.*;
import static io.github.up2jakarta.dte.exe.script.ClassUtil.*;

public class VisitorUtilTests {

    @Test
    public void shouldAssignableToByte() {
        assertTrue(isAssignable(byte.class, Byte.class));
        assertTrue(isAssignable(short.class, Byte.class));
        assertTrue(isAssignable(int.class, Byte.class));
        assertTrue(isAssignable(long.class, Byte.class));
        assertTrue(isAssignable(float.class, Byte.class));
        assertTrue(isAssignable(double.class, Byte.class));
    }

    @Test
    public void shouldNotAssignableToByte() {
        assertFalse(isAssignable(String.class, Byte.class));
    }

    @Test
    public void shouldAssignableToShort() {
        assertTrue(isAssignable(short.class, short.class));
        assertTrue(isAssignable(int.class, short.class));
        assertTrue(isAssignable(long.class, short.class));
        assertTrue(isAssignable(float.class, short.class));
        assertTrue(isAssignable(double.class, short.class));
    }

    @Test
    public void shouldNotAssignableToShort() {
        assertFalse(isAssignable(Byte.class, short.class));
    }

    @Test
    public void shouldAssignableToInteger() {
        assertTrue(isAssignable(int.class, Integer.class));
        assertTrue(isAssignable(long.class, Integer.class));
        assertTrue(isAssignable(float.class, Integer.class));
        assertTrue(isAssignable(double.class, Integer.class));
    }

    @Test
    public void shouldNotAssignableToInteger() {
        assertFalse(isAssignable(Byte.class, Integer.class));
        assertFalse(isAssignable(Short.class, Integer.class));
    }

    @Test
    public void shouldAssignableToLong() {
        assertTrue(isAssignable(long.class, Long.class));
        assertTrue(isAssignable(float.class, Long.class));
        assertTrue(isAssignable(double.class, Long.class));
    }

    @Test
    public void shouldNotAssignableToLong() {
        assertFalse(isAssignable(Byte.class, Long.class));
        assertFalse(isAssignable(Short.class, Long.class));
        assertFalse(isAssignable(Integer.class, Long.class));
    }

    @Test
    public void shouldAssignableToDate() {
        assertTrue(isAssignable(java.util.Date.class, java.sql.Date.class));
    }

    @Test
    public void shouldAssignableToFloat() {
        assertTrue(isAssignable(float.class, float.class));
        assertTrue(isAssignable(double.class, float.class));
    }

    @Test
    public void shouldNotAssignableToFloat() {
        assertFalse(isAssignable(int.class, float.class));
        assertFalse(isAssignable(long.class, float.class));
    }

    @Test
    public void shouldAssignableToDouble() {
        assertTrue(isAssignable(double.class, float.class));
        assertTrue(isAssignable(double.class, int.class));
    }

    @Test
    public void shouldAssignableToNull() {
        assertTrue(isAssignable(Object.class, null));
    }

    @Test
    public void shouldNotAssignableToDouble() {
        assertFalse(isAssignable(float.class, double.class));
    }

    @Test
    public void shouldWrapPrimitives() {
        assertSame(Byte.class, wrap(byte.class));
        assertSame(Integer.class, wrap(int.class));
        assertSame(Double.class, wrap(double.class));
        assertSame(Boolean.class, wrap(boolean.class));
        assertSame(Character.class, wrap(char.class));
        assertSame(Short.class, wrap(short.class));
        assertSame(Float.class, wrap(float.class));
        assertSame(Long.class, wrap(long.class));
        assertSame(Void.class, wrap(void.class));
        assertSame(null, wrap(null));
    }

    @Test
    public void shouldWrapWrapArrays() {
        assertSame(Byte[].class, wrap(byte[].class));
        assertSame(Integer[].class, wrap(int[].class));
        assertSame(Double[].class, wrap(double[].class));
        assertSame(Boolean[].class, wrap(boolean[].class));
        assertSame(Character[].class, wrap(char[].class));
        assertSame(Short[].class, wrap(short[].class));
        assertSame(Float[].class, wrap(float[].class));
        assertSame(Long[].class, wrap(long[].class));
    }

    @Test
    public void shouldFindSuperClass() {
        assertSame(Object.class, firstSuperClass(null, Integer.class));
        assertSame(Object.class, firstSuperClass(Integer.class, null));
        assertSame(Number.class, firstSuperClass(Byte.class, Integer.class));
        assertSame(Long.class, firstSuperClass(Long.class, Long.class));
        assertSame(java.util.Date.class, firstSuperClass(java.util.Date.class, java.sql.Date.class));
        assertSame(java.util.Date.class, firstSuperClass(java.sql.Date.class, java.util.Date.class));
    }

    @Test
    public void shouldFindCommonSuperClass() {
        assertSame(Integer.class, commonSuperClass(null, Integer.class));
        assertSame(Integer.class, commonSuperClass(Integer.class, null));
        assertSame(Number.class, commonSuperClass(Byte.class, Integer.class));
    }

}
