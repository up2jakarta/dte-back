package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.DayOfWeek;

/**
 * {@link DayOfWeek} extension methods.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class DayOfWeekExtension {

    private DayOfWeekExtension() {
    }

    /**
     * Returns {@code true} if this day of the week is a weekend day (Saturday or Sunday).
     *
     * @param self a DayOfWeek
     * @return true if this DayOfWeek is Saturday or Sunday
     */
    public static boolean isWeekend(final DayOfWeek self) {
        return self == DayOfWeek.SATURDAY || self == DayOfWeek.SUNDAY;
    }

    /**
     * Returns {@code true} if the DayOfWeek is a weekday.
     *
     * @param self a DayOfWeek
     * @return true if this DayOfWeek is Monday through Friday
     */
    public static boolean isWeekday(final DayOfWeek self) {
        return !isWeekend(self);
    }
}
