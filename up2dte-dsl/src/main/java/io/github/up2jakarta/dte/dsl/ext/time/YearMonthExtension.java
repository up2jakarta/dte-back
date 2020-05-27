package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.LocalDate;
import java.time.Period;
import java.time.YearMonth;
import java.time.temporal.ChronoUnit;

/**
 * {@link YearMonth} extension methods.
 */
@SuppressWarnings({"unused"})
public final class YearMonthExtension {

    private YearMonthExtension() {
    }

    /**
     * Returns a {@link java.time.YearMonth} that is {@code months} months after this year/month.
     *
     * @param self   a YearMonth
     * @param months the number of months to add
     * @return a Year
     */
    public static YearMonth plus(final YearMonth self, final long months) {
        return self.plusMonths(months);
    }

    /**
     * Returns a {@link java.time.YearMonth} that is {@code months} months before this year/month.
     *
     * @param self   a YearMonth
     * @param months the number of months to subtract
     * @return a Year
     */
    public static YearMonth minus(final YearMonth self, final long months) {
        return self.minusMonths(months);
    }

    /**
     * Returns a {@link java.time.Period} between the {@code other} (inclusive) and {@code self} (exclusive).
     *
     * @param self  a YearMonth
     * @param other another YearMonth
     * @return a Period
     */
    public static Period minus(final YearMonth self, final YearMonth other) {
        return Period.ZERO.plusMonths(ChronoUnit.MONTHS.between(other, self)).normalized();
    }

    /**
     * Returns a {@link java.time.YearMonth} that is the month after this year/month.
     *
     * @param self a YearMonth
     * @return the next YearMonth
     */
    public static YearMonth next(final YearMonth self) {
        return plus(self, 1);
    }

    /**
     * Returns a {@link java.time.YearMonth} that is the month before this year/month.
     *
     * @param self a YearMonth
     * @return the previous YearMonth
     */
    public static YearMonth previous(final YearMonth self) {
        return minus(self, 1);
    }

    /**
     * Returns a {@link java.time.LocalDate} of this year/month and the given day of the month.
     *
     * @param self       a YearMonth
     * @param dayOfMonth a day of the month
     * @return a LocalDate
     */
    public static LocalDate or(final YearMonth self, final int dayOfMonth) {
        return self.atDay(dayOfMonth);
    }
}
