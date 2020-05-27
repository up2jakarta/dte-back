package io.github.up2jakarta.dte.exe.script;

import java.lang.reflect.Array;

/**
 * Utilities Class that contains useful methods for class manipulation.
 *
 * @author A.ABBESSI
 * @see #isAssignable(Class, Class)
 * @see #firstSuperClass(Class, Class)
 * @see #wrap(Class)
 * @see #commonSuperClass(Class, Class)
 */
@SuppressWarnings("WeakerAccess")
public final class ClassUtil {

    private static final Class<?>[] WRAPPERS = {
            Integer.class,
            Double.class,
            Byte.class,
            Boolean.class,
            Character.class,
            Void.class,
            Short.class,
            Float.class,
            Long.class
    };

    /**
     * Private constructor.
     */
    private ClassUtil() {
    }

    /**
     * Return the first common superclass of the given two classes.
     *
     * @param c1 the class 1
     * @param c2 the class 2
     * @return the common superclass
     */
    public static Class<?> firstSuperClass(final Class<?> c1, final Class<?> c2) {
        if (c1 == null || c2 == null) {
            return Object.class;
        }
        if (c1.equals(c2)) {
            return c1;
        }
        Class<?> result = firstSuperClass(c1.getSuperclass(), c2);
        if (!result.equals(Object.class)) {
            return result;
        }
        result = firstSuperClass(c1, c2.getSuperclass());
        if (!result.equals(Object.class)) {
            return result;
        }
        return firstSuperClass(c1.getSuperclass(), c2.getSuperclass());
    }

    /**
     * Determines if the class or interface represented by the given class {@code x}
     * object is either the same as, or is a superclass or superinterface of,
     * the class or interface represented by the specified {@code y} parameter.
     *
     * @param x the first class
     * @param y the second class
     * @return the {@code boolean} value indicating whether objects of the
     * type {@code y} can be assigned to objects of {@code x} class
     */
    public static boolean isAssignable(final Class<?> x, final Class<?> y) {
        if (y == null) {
            return true;
        }
        final Class<?> a = x.isPrimitive() ? wrap(x) : x;
        final Class<?> b = y.isPrimitive() ? wrap(y) : y;
        if (a.isAssignableFrom(b)) {
            return true;
        }
        // Automatic cast
        if (b == Byte.class) {
            return a == Short.class || a == Integer.class || a == Long.class || a == Float.class || a == Double.class;
        }
        if (b == Short.class) {
            return a == Integer.class || a == Long.class || a == Float.class || a == Double.class;
        }
        if (b == Integer.class) {
            return a == Long.class || a == Float.class || a == Double.class;
        }
        if (b == Long.class) {
            return a == Float.class || a == Double.class;
        }
        return b == Float.class && a == Double.class;
    }

    /**
     * Wraps a primitive class to the corresponding wrapper.
     *
     * @param type the class
     * @return the class wrapper
     */
    public static Class<?> wrap(final Class<?> type) {
        if (type == null || (!type.isPrimitive() && !type.isArray())) {
            return type;
        }
        if (type.isArray()) {
            return Array.newInstance(wrap(type.getComponentType()), 0).getClass();
        }
        final String name = type.getName();
        final int c0 = name.charAt(0);
        final int c2 = name.charAt(2);
        final int mapper = (c0 + c0 + c0 + 5) & (118 - c2);
        return WRAPPERS[mapper];
    }

    /**
     * Find the first common super class of the given two classes.
     * If {@code a} is equals to {@code null}, then returns {@code b}.
     * If {@code b} is equals to {@code null}, then returns {@code a}.
     *
     * @param a the first class
     * @param b the second class
     * @return the common super class, otherwise {@code Object.class}
     */
    public static Class<?> commonSuperClass(final Class<?> a, final Class<?> b) {
        if (a == null) {
            return b;
        }
        if (b == null) {
            return a;
        }
        return firstSuperClass(wrap(a), wrap(b));
    }

}
