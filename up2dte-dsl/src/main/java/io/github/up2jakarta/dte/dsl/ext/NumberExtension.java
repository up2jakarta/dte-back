package io.github.up2jakarta.dte.dsl.ext;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * {@link Number} extension methods.
 */
@SuppressWarnings({"unused"})
public final class NumberExtension {

    private NumberExtension() {
    }

    /**
     * Round down the specified {@code Double} with the given {@code scale}.
     *
     * @param v the double value
     * @param s the scale
     * @return a {@code BigDecimal} whose scale is the specified value
     */
    public static BigDecimal round(final Double v, final int s) {
        return BigDecimal.valueOf(v).setScale(s, RoundingMode.DOWN);
    }

    /**
     * Round down the specified {@code Double} with the given {@code scale}.
     *
     * @param v the double value
     * @param s the scale
     * @return a {@code BigDecimal} whose scale is the specified value
     */
    public static BigDecimal round(final Float v, final int s) {
        return BigDecimal.valueOf(v).setScale(s, RoundingMode.DOWN);
    }

    /**
     * Round down the specified {@code BigDecimal} with the given {@code scale}.
     *
     * @param v the double value
     * @param s the scale
     * @return a {@code BigDecimal} whose scale is the specified value
     */
    public static BigDecimal round(final BigDecimal v, final int s) {
        return v.setScale(s, RoundingMode.DOWN);
    }
}
