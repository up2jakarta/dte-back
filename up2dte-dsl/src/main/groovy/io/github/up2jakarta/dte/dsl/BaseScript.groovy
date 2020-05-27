package io.github.up2jakarta.dte.dsl

import java.lang.reflect.Array

/**
 * Script base class that defines Engine domain-specific language (DSL).
 * <p>
 * @link <a href="http://docs.groovy-lang.org/docs/latest/html/documentation/core-domain-specific-languages.html#_script_base_classes"  >  </a>
 * @link <a href="https://docs.groovy-lang.org/latest/html/documentation/#_domain_specific_languages"  > </a>
 * @link <a href="https://docs.groovy-lang.org/latest/html/documentation/#_operator_overloading" > </a>
 */
@SuppressWarnings("unused")
abstract class BaseScript extends Script {

    /**
     * Tests if a variable is defined in the script context
     */
    boolean has(final String property) {
        binding.variables.containsKey(property)
    }

    /**
     * Test if the given {@code value} is null or an empty {@code String} or {@code Array} or {@code Collection}.
     */
    static boolean empty(Object value) {
        if (value == null || "" == value) {
            return true
        }
        if (value instanceof Collection) {
            return value.size() == 0
        }
        if (value instanceof Map) {
            return value.size() == 0
        }
        if (value.class.isArray()) {
            return Array.getLength(value) == 0
        }
        return false
    }

    /**
     * Returns the first non null value in the given {@code values}
     */
    static <T> T nvl(T v1, T v2, T... values) {
        if (v1 != null) {
            return v1
        }
        if (v2 != null) {
            return v2
        }
        for (value in values) {
            if (value != null) {
                return value
            }
        }
        return null
    }

    /**
     * Throw a business exception with a formatted message using the specified {@code format} and
     * the given {@code arguments}.
     * @param format the message format
     * @param args the message arguments
     */
    void error(final String format, final Object... args) {
        final String msg = String.format(format, args)
        throw new BusinessException(metaClass.theClass.getSimpleName(), msg)
    }
}
