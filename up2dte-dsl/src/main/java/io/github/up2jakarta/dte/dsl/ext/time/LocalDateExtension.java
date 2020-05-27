package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;
import java.time.temporal.TemporalAdjusters;

/**
 * {@link LocalDate} extension methods.
 */
@SuppressWarnings("unused")
public final class LocalDateExtension {

    private LocalDateExtension() {
    }

    /**
     * Returns a {@link LocalDate} that is {@code days} days after this date.
     *
     * @param self a LocalDate
     * @param days the number of days to add
     * @return a LocalDate
     */
    public static LocalDate plus(final LocalDate self, final long days) {
        return self.plusDays(days);
    }

    /**
     * Returns a {@link LocalDate} that is {@code period} after this date.
     *
     * @param self   a LocalDate
     * @param period the period to add
     * @return a LocalDate
     */
    public static LocalDate plus(final LocalDate self, final Period period) {
        return self.plus(period);
    }

    /**
     * Returns a {@link LocalDate} that is {@code days} days before this date.
     *
     * @param self a LocalDate
     * @param days the number of days to subtract
     * @return a LocalDate
     */
    public static LocalDate minus(final LocalDate self, final long days) {
        return self.minusDays(days);
    }

    /**
     * Returns a {@link LocalDate} that is {@code period} before this date.
     *
     * @param self   a LocalDate
     * @param period the period to subtract
     * @return a LocalDate
     */
    public static LocalDate minus(final LocalDate self, final Period period) {
        return self.minus(period);
    }

    /**
     * Returns a {@link java.time.Period} between the {@code self} (inclusive) and {@code other} (exclusive).
     *
     * @param self  a date
     * @param other another date
     * @return a LocalDate
     */
    public static Period minus(final LocalDate self, final LocalDate other) {
        return Period.between(other, self);
    }

    /**
     * Returns a {@link java.time.LocalDate} one day after this date.
     *
     * @param self a LocalDate
     * @return the next day
     */
    public static LocalDate next(final LocalDate self) {
        return self.plusDays(1);
    }

    /**
     * Returns a {@link java.time.LocalDate} one day before this date.
     *
     * @param self a LocalDate
     * @return the previous day
     */
    public static LocalDate previous(final LocalDate self) {
        return self.minusDays(1);
    }

    /**
     * Returns a {@link java.time.LocalDateTime} from this date and the provided {@link java.time.LocalTime}.
     *
     * @param self a LocalDate
     * @param time a LocalTime
     * @return a LocalDateTime
     */
    public static LocalDateTime or(final LocalDate self, final LocalTime time) {
        return LocalDateTime.of(self, time);
    }

    /**
     * Returns a {@link java.time.OffsetDateTime} from this date and the provided {@link java.time.OffsetTime}.
     *
     * @param self a LocalDate
     * @param time an OffsetTime
     * @return an OffsetDateTime
     */
    public static OffsetDateTime or(final LocalDate self, final OffsetTime time) {
        return time.atDate(self);
    }

    /* Adjusters */

    /**
     * Returns a new date set to the first day of the next month.
     *
     * @param self a localDate
     * @return the first day-of-next-month date
     */
    public static LocalDate firstDayOfNextMonth(final LocalDate self) {
        return self.with(TemporalAdjusters.firstDayOfNextMonth());
    }

    /**
     * Returns a new date set to the first day of the next year.
     *
     * @param self a localDate
     * @return the first day-of-next-year date
     */
    public static LocalDate firstDayOfNextYear(final LocalDate self) {
        return self.with(TemporalAdjusters.firstDayOfNextYear());
    }

    /**
     * Returns a new date set to the first day of the current month.
     *
     * @param self a localDate
     * @return the first day-of-month date
     */
    public static LocalDate firstDayOfMonth(final LocalDate self) {
        return self.with(TemporalAdjusters.firstDayOfMonth());
    }

    /**
     * Returns a new date set to the first day of the next year.
     *
     * @param self a localDate
     * @return the first day-of-year date
     */
    public static LocalDate firstDayOfYear(final LocalDate self) {
        return self.with(TemporalAdjusters.firstDayOfYear());
    }

    /**
     * Returns a new date set to the lst day of the current year.
     *
     * @param self a localDate
     * @return the last day-of-year date
     */
    public static LocalDate lastDayOfYear(final LocalDate self) {
        return self.with(TemporalAdjusters.lastDayOfYear());
    }

    /**
     * Returns a new date set to the last day of the current month.
     *
     * @param self a localDate
     * @return the last day-of-month date
     */
    public static LocalDate lastDayOfMonth(final LocalDate self) {
        return self.with(TemporalAdjusters.lastDayOfMonth());
    }

    /**
     * Returns a new date in the same month with the last matching day-of-week.
     * This is used for expressions like 'last Tuesday in March'.
     *
     * @param self a localDate
     * @param day  a day-of-week
     * @return the last in-month date
     */
    public static LocalDate lastInMonth(final LocalDate self, final DayOfWeek day) {
        return self.with(TemporalAdjusters.lastInMonth(day));
    }

    /**
     * Returns a new date in the same month with the first matching day-of-week.
     * This is used for expressions like 'last Tuesday in March'.
     *
     * @param self a localDate
     * @param day  a day-of-week
     * @return the first in-month date
     */
    public static LocalDate firstInMonth(final LocalDate self, final DayOfWeek day) {
        return self.with(TemporalAdjusters.dayOfWeekInMonth(1, day));
    }

    /**
     * Returns the first occurrence of the specified day-of-week after the date being adjusted.
     *
     * @param self a localDate
     * @param day  the day-of-week to move the date to
     * @return the next day-of-week date
     */
    public static LocalDate next(final LocalDate self, final DayOfWeek day) {
        return self.with(TemporalAdjusters.next(day));
    }

    /**
     * Returns the first occurrence of the specified day-of-week before the date being adjusted.
     *
     * @param self a localDate
     * @param day  the day-of-week to move the date to
     * @return the next day-of-week date
     */
    public static LocalDate previous(final LocalDate self, final DayOfWeek day) {
        return self.with(TemporalAdjusters.previous(day));
    }

    /**
     * Returns the first occurrence of the specified day-of-week after the date being adjusted
     * unless it is already on that day in which case the same object is returned.
     *
     * @param self a localDate
     * @param day  the day-of-week to move the date to
     * @return the next-or-same day-of-week date
     */
    public static LocalDate nextOrSame(final LocalDate self, final DayOfWeek day) {
        return self.with(TemporalAdjusters.nextOrSame(day));
    }

    /**
     * Returns the first occurrence of the specified day-of-week before the date being adjusted
     * unless it is already on that day in which case the same object is returned.
     *
     * @param self a localDate
     * @param day  the day-of-week to move the date to
     * @return the previous-or-same day-of-week date
     */
    public static LocalDate previousOrSame(final LocalDate self, final DayOfWeek day) {
        return self.with(TemporalAdjusters.previousOrSame(day));
    }

}
