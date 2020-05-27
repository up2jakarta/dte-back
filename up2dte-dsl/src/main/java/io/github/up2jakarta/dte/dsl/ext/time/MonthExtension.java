package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.Month;
import java.time.MonthDay;
import java.time.Year;
import java.time.YearMonth;

/**
 * {@link Month} extension methods.
 */
@SuppressWarnings("unused")
public final class MonthExtension {

    private MonthExtension() {
    }

    /**
     * Creates a {@link java.time.MonthDay} at the provided day of the month.
     *
     * @param self       a Month
     * @param dayOfMonth a day of the month
     * @return a MonthDay
     */
    public static MonthDay or(final Month self, final int dayOfMonth) {
        return MonthDay.of(self, dayOfMonth);
    }

    /**
     * Creates a {@link java.time.YearMonth} at the provided {@link java.time.Year}.
     *
     * @param self a Month
     * @param year a Year
     * @return a YearMonth
     */
    public static YearMonth or(final Month self, final Year year) {
        return YearMonth.of(year.getValue(), self);
    }

}
