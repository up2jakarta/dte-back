package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.LocalDate;
import java.time.MonthDay;
import java.time.Year;

/**
 * {@link MonthDay} extension methods.
 */
@SuppressWarnings("unused")
public final class MonthDayExtension {

    private MonthDayExtension() {
    }

    /**
     * Returns a {@link java.time.LocalDate} of this month/day and the provided year.
     *
     * @param self a MonthDay
     * @param year a year
     * @return a LocalDate
     */
    public static LocalDate or(final MonthDay self, final int year) {
        return self.atYear(year);
    }

    /**
     * Returns a {@link java.time.LocalDate} of this month/day and the provided {@link java.time.Year}.
     *
     * @param self a MonthDay
     * @param year a Year
     * @return a LocalDate
     */
    public static LocalDate or(final MonthDay self, final Year year) {
        return year.atMonthDay(self);
    }
}
