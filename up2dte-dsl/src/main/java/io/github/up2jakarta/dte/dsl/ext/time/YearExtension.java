package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

import static java.time.temporal.ChronoUnit.YEARS;

/**
 * {@link Year} extension methods.
 */
@SuppressWarnings({"unused"})
public final class YearExtension {

    private YearExtension() {
    }

    /**
     * Returns a {@link java.time.Year} that is {@code years} years after this year.
     *
     * @param self  a Year
     * @param years the number of years to add
     * @return a Year
     */
    public static Year plus(final Year self, final long years) {
        return self.plusYears(years);
    }

    /**
     * Returns a {@link java.time.Year} that is {@code years} years before this year.
     *
     * @param self  a Year
     * @param years the number of years to subtract
     * @return a Year
     */
    public static Year minus(final Year self, final long years) {
        return self.minusYears(years);
    }

    /**
     * Returns a {@link java.time.Period} between the {@code self} (inclusive) and {@code other} (exclusive).
     *
     * @param self  a Year
     * @param other another Year
     * @return a Period between the Years
     */
    public static Period minus(final Year self, final Year other) {
        return Period.ZERO.plusYears(YEARS.between(other, self));
    }

    /**
     * Returns a {@link java.time.Year} after this year.
     *
     * @param self a Year
     * @return the next Year
     */
    public static Year next(final Year self) {
        return plus(self, 1);
    }

    /**
     * Returns a {@link java.time.Year} before this year.
     *
     * @param self a Year
     * @return the previous Year
     */
    public static Year previous(final Year self) {
        return minus(self, 1);
    }

    /**
     * Returns a {@link java.time.YearMonth} of this year and the provided {@link java.time.Month}.
     *
     * @param self  a Year
     * @param month a Month
     * @return a YearMonth
     */
    public static YearMonth or(final Year self, final Month month) {
        return self.atMonth(month);
    }

    /**
     * Returns a {@link java.time.LocalDate} of this year on the given {@link java.time.MonthDay}.
     *
     * @param self     a Year
     * @param monthDay a MonthDay
     * @return a LocalDate
     */
    public static LocalDate or(final Year self, final MonthDay monthDay) {
        return self.atMonthDay(monthDay);
    }

}
