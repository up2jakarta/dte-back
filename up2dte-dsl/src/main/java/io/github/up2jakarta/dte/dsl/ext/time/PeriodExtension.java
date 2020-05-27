package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;
import java.time.temporal.ChronoUnit;

import static java.lang.Math.abs;

/**
 * {@link Period} extension methods.
 */
@SuppressWarnings({"unused"})
public final class PeriodExtension {

    private PeriodExtension() {
    }

    /**
     * Returns a {@link java.time.Period} that is {@code days} days longer than this period.
     * No normalization is performed.
     *
     * @param self a Period
     * @param days the number of days to increase this Period by
     * @return a Period
     */
    public static Period plus(final Period self, final long days) {
        return self.plusDays(days);
    }

    /**
     * Returns a {@link LocalDate} that is {@code period} after this date.
     *
     * @param date a localDate
     * @param self the period to add
     * @return a LocalDate
     */
    public static LocalDate plus(final Period self, final LocalDate date) {
        return date.plus(self);
    }

    /**
     * Returns a copy of {@code self} with the specified {@code period} added.
     *
     * @param self the current {@code Period}
     * @param date the specified {@code LocalDateTime} that the period to add to
     * @return a {@code LocalDateTime} based on this date-time with the addition made
     */
    public static LocalDateTime plus(final Period self, final LocalDateTime date) {
        return date.plus(self);
    }

    /**
     * Returns a copy of the provided {@code date} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param date the the specified {@code ZonedDateTime}
     * @return a {@code ZonedDateTime} based on this date-time with the addition made
     */
    public static ZonedDateTime plus(final Period self, final ZonedDateTime date) {
        return date.plus(self);
    }

    /**
     * Returns a copy of the provided {@code date} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param date the the specified {@code OffsetDateTime}
     * @return a {@code OffsetDateTime} based on this date-time with the addition made
     */
    public static OffsetDateTime plus(final Period self, final OffsetDateTime date) {
        return date.plus(self);
    }

    /**
     * Returns a {@link java.time.Period} that is {@code days} days shorter than this period.
     * No normalization is performed.
     *
     * @param self a Period
     * @param days the number of days to decrease this Period by
     * @return a Period
     */
    public static Period minus(final Period self, final long days) {
        return self.minusDays(days);
    }

    /**
     * Returns a {@link java.time.Period} that is one day longer than this period.
     * No normalization is performed.
     *
     * @param self a Period
     * @return a Period one day longer in length
     */
    public static Period next(final Period self) {
        return plus(self, 1);
    }

    /**
     * Returns a {@link java.time.Period} that is one day shorter than this period.
     * No normalization is performed.
     *
     * @param self a Period
     * @return a Period one day shorter in length
     */
    public static Period previous(final Period self) {
        return minus(self, 1);
    }

    /**
     * Supports the unary minus operator; equivalent to calling the {@link java.time.Period#negated()} method.
     *
     * @param self a Period
     * @return a negated Period
     */
    public static Period negative(final Period self) {
        return self.negated();
    }

    /**
     * Supports the unary plus operator; returns a {@link java.time.Period} with all unit values positive.
     * For example, a period of "2 years, -3 months, and -4 days" would result in a period of
     * "2 years, 3 months, and 4 days." No normalization is performed.
     *
     * @param self a Period
     * @return a positive Period
     */
    public static Period positive(final Period self) {
        return self.isNegative() ? Period.of(abs(self.getYears()), abs(self.getMonths()), abs(self.getDays())) : self;
    }

    /**
     * Supports the multiply operator; equivalent to calling the {@link java.time.Period#multipliedBy(int)} method.
     *
     * @param self   a Period
     * @param scalar a scalar to multiply each unit by
     * @return a Period
     */
    public static Period multiply(final Period self, final int scalar) {
        return self.multipliedBy(scalar);
    }

    /**
     * Returns the current date with the specified period subtracted.
     * For example the following expression '1.day.ago' means 'yesterday'
     * .
     * Returns the current date with the specified number of days added.
     *
     * @param self a Period
     * @return a date equals the specified-period ago
     */
    public static LocalDate getAgo(final Period self) {
        return LocalDate.now().minus(self);
    }

    /**
     * Returns the current date with the specified period added.
     * This is used for expressions like '1.day.from' to say 'tomorrow'
     * .
     * Returns the current date with the specified number of days added.
     *
     * @param self a Period
     * @return a date equals the specified-period from
     */
    public static LocalDate getLater(final Period self) {
        return LocalDate.now().plus(self);
    }

    /**
     * Calculates the total number of days of the {@code self} period which starts from the given {@code first} date.
     *
     * @param self  the current {@code Period}
     * @param first the starting date inclusive
     * @return the total number of days
     */
    public static long rightShift(final Period self, final LocalDate first) {
        if (self.getYears() == 0 && self.getMonths() == 0) {
            return self.getDays();
        }
        return ChronoUnit.DAYS.between(first, first.plus(self));
    }

    /**
     * Calculates the total number of days of the {@code self} period which ends at the given {@code last} date.
     *
     * @param self the current {@code Period}
     * @param last the ending date exclusive
     * @return the total number of days
     */
    public static long leftShift(final Period self, final LocalDate last) {
        if (self.getYears() == 0 && self.getMonths() == 0) {
            return self.getDays();
        }
        return ChronoUnit.DAYS.between(last.minus(self), last);
    }

}
