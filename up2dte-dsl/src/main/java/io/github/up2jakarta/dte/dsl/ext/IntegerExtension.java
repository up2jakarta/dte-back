package io.github.up2jakarta.dte.dsl.ext;

import java.time.*;

/**
 * {@link Integer} extension methods.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class IntegerExtension {

    private IntegerExtension() {
    }

    /* ********************************** Period constructors ********************************** */

    /**
     * Obtains a {@code Duration} representing a number of standard hours.
     *
     * @param hours the number of hours, positive or negative
     * @return a duration
     */
    public static Duration getHours(final Integer hours) {
        return Duration.ofHours(hours);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard minutes.
     *
     * @param minutes the number of minutes, positive or negative
     * @return a duration
     */
    public static Duration getMinutes(final Integer minutes) {
        return Duration.ofMinutes(minutes);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard seconds.
     *
     * @param seconds the number of seconds, positive or negative
     * @return a duration
     */
    public static Duration getSeconds(final Integer seconds) {
        return Duration.ofSeconds(seconds);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard milliseconds.
     *
     * @param millis the number of milliseconds, positive or negative
     * @return a duration
     */
    public static Duration getMillis(final Integer millis) {
        return Duration.ofMillis(millis);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard nanoseconds.
     *
     * @param nanos the number of nanoseconds, positive or negative
     * @return a duration
     */
    public static Duration getNanos(final Integer nanos) {
        return Duration.ofNanos(nanos);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard hours.
     *
     * @param hours the number of hours, positive or negative
     * @return a duration
     */
    public static Duration getHour(final Integer hours) {
        return getHours(hours);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard minutes.
     *
     * @param minutes the number of minutes, positive or negative
     * @return a duration
     */
    public static Duration getMinute(final Integer minutes) {
        return getMinutes(minutes);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard seconds.
     *
     * @param seconds the number of seconds, positive or negative
     * @return a duration
     */
    public static Duration getSecond(final Integer seconds) {
        return Duration.ofSeconds(seconds);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard milliseconds.
     *
     * @param millis the number of milliseconds, positive or negative
     * @return a duration
     */
    public static Duration getMilli(final Integer millis) {
        return Duration.ofMillis(millis);
    }

    /**
     * Obtains a {@code Duration} representing a number of standard nanoseconds.
     *
     * @param nanos the number of nanoseconds, positive or negative
     * @return a duration
     */
    public static Duration getNano(final Integer nanos) {
        return Duration.ofNanos(nanos);
    }

    /* ********************************** Period constructors ********************************** */

    /**
     * Obtains a {@code Period} representing a number of days.
     *
     * @param days the number of days, positive or negative
     * @return the period of days
     */
    public static Period getDays(final Integer days) {
        return Period.ofDays(days);
    }

    /**
     * Obtains a {@code Period} representing a number of weeks.
     *
     * @param weeks the number of weeks, positive or negative
     * @return the period of days
     */
    public static Period getWeeks(final Integer weeks) {
        return Period.ofWeeks(weeks);
    }

    /**
     * Obtains a {@code Period} representing a number of months.
     *
     * @param months the number of weeks, positive or negative
     * @return the period of months
     */
    public static Period getMonths(final Integer months) {
        return Period.ofMonths(months);
    }

    /**
     * Obtains a {@code Period} representing a number of years.
     *
     * @param years the number of years, positive or negative
     * @return the period of months
     */
    public static Period getYears(final Integer years) {
        return Period.ofYears(years);
    }

    /**
     * Obtains a {@code Period} representing a number of days.
     *
     * @param days the number of days, positive or negative
     * @return the period of days
     */
    public static Period getDay(final Integer days) {
        return getDays(days);
    }

    /**
     * Obtains a {@code Period} representing a number of weeks.
     *
     * @param weeks the number of weeks, positive or negative
     * @return the period of days
     */
    public static Period getWeek(final Integer weeks) {
        return getWeeks(weeks);
    }

    /**
     * Obtains a {@code Period} representing a number of months.
     *
     * @param months the number of weeks, positive or negative
     * @return the period of months
     */
    public static Period getMonth(final Integer months) {
        return getMonths(months);
    }

    /**
     * Obtains a {@code Period} representing a number of years.
     *
     * @param years the number of years, positive or negative
     * @return the period of months
     */
    public static Period getYear(final Integer years) {
        return getYears(years);
    }

    /* ********************************** Number operations ********************************** */

    /**
     * Returns a {@link LocalDate} that is {@code days} days after this date.
     *
     * @param days a integer
     * @param date a local date
     * @return a LocalDate
     */
    public static LocalDate plus(final Integer days, final LocalDate date) {
        return date.plusDays(days);
    }

    /**
     * Returns a {@link Duration} that is {@code seconds} seconds longer than this duration.
     *
     * @param duration the duration
     * @param seconds  the number of seconds to add
     * @return a duration
     */
    public static Duration plus(final Integer seconds, final Duration duration) {
        return duration.plusSeconds(seconds);
    }

    /**
     * Supports the multiplication operator; equivalent to calling the {@link Duration#multipliedBy(long)} method.
     *
     * @param duration a duration
     * @param scalar   the value to multiply by
     * @return a duration
     */
    public static Duration multiply(final Integer scalar, final Duration duration) {
        return duration.multipliedBy(scalar);
    }

    /**
     * Returns a {@link Period} that is {@code days} days after this date.
     *
     * @param days   a integer
     * @param period a local date
     * @return a localDate
     */
    public static Period plus(final Integer days, final Period period) {
        return period.plusDays(days);
    }

    /**
     * Supports the multiply operator; equivalent to calling the {@link Period#multipliedBy(int)} method.
     *
     * @param period a period
     * @param scalar a scalar to multiply each unit by
     * @return a period
     */
    public static Period multiply(final Integer scalar, final Period period) {
        return period.multipliedBy(scalar);
    }

    /**
     * Returns a {@link LocalTime} that is {@code seconds} seconds after this time.
     *
     * @param time    a LocalTime
     * @param seconds the number of seconds to add
     * @return a LocalTime
     */
    public static LocalTime plus(final Integer seconds, final LocalTime time) {
        return time.plusSeconds(seconds);
    }

    /**
     * Returns a {@link LocalDateTime} that is {@code seconds} seconds after this date-time.
     *
     * @param time    the specified {@code LocalDateTime}
     * @param seconds the number of seconds to add
     * @return a {@link LocalDateTime}
     */
    public static LocalDateTime plus(final Integer seconds, final LocalDateTime time) {
        return time.plusSeconds(seconds);
    }

    /**
     * Returns a copy of the specified {@code YearMonth} with the specified number of months added.
     *
     * @param self     the months to add, may be negative
     * @param temporal the specified {@code YearMonth}
     * @return a {@code YearMonth} based on the specified year-month with the months added, not null
     */
    public static YearMonth plus(final Integer self, final YearMonth temporal) {
        return temporal.plusMonths(self);
    }

    /**
     * Returns a {@link Year} that is {@code self} years after the specified year.
     *
     * @param self the numbers of years to add
     * @param year the reference year
     * @return a {@code Year} based on given year with the the current number added,
     */
    public static Year plus(final Integer self, final Year year) {
        return year.plusYears(self);
    }

    /**
     * Returns the {@link Month} that is {@code self} months after the given {@code month}.
     *
     * @param self  the number of months move forward
     * @param month the month of year
     * @return the Month
     */
    public static Month plus(final Integer self, final Month month) {
        return month.plus(self);
    }

    /**
     * Returns the day-of-week that is the current number of days after the specified {@code day} of week.
     *
     * @param self the days to add, positive or negative
     * @param day  the day of week
     * @return the resulting day-of-week
     */
    public static DayOfWeek plus(final Integer self, final DayOfWeek day) {
        return day.plus(self);
    }

    /**
     * Returns an {@link OffsetTime} that is {@code seconds} seconds after this time.
     *
     * @param time an OffsetTime
     * @param self the number of seconds to add
     * @return an OffsetTime
     */
    public static OffsetTime plus(final Integer self, final OffsetTime time) {
        return time.plusSeconds(self);
    }

    /**
     * Returns a copy of the provided {@code temporal} with the specified {@code self} added.
     *
     * @param temporal an OffsetDateTime
     * @param self     the number of seconds to add
     * @return an OffsetDateTime
     */
    public static OffsetDateTime plus(final Integer self, final OffsetDateTime temporal) {
        return temporal.plusSeconds(self);
    }

    /**
     * Returns an {@link ZonedDateTime} that is {@code seconds} seconds after this date/time.
     *
     * @param temporal an ZonedDateTime
     * @param self     the number of seconds to add
     * @return an OffsetDateTime
     */
    public static ZonedDateTime plus(final Integer self, final ZonedDateTime temporal) {
        return temporal.plusSeconds(self);
    }

    /**
     * Returns an {@link Instant} that is {@code seconds} seconds after this date/time.
     *
     * @param temporal an Instant
     * @param self     the number of seconds to add
     * @return an Instant
     */
    public static Instant plus(final Integer self, final Instant temporal) {
        return temporal.plusSeconds(self);
    }

    /**
     * Returns a {@link LocalDate} of this month/day and the provided year.
     *
     * @param self     the number of years to add
     * @param monthDay a month-day
     * @return a LocalDate
     */
    public static LocalDate or(final Integer self, final MonthDay monthDay) {
        return monthDay.atYear(self);
    }

    /**
     * Creates a {@link MonthDay} at the provided day of the month.
     *
     * @param self  the day of month
     * @param month a month
     * @return a MonthDay
     */
    public static MonthDay or(final Integer self, final Month month) {
        return MonthDay.of(month, self);
    }

    /**
     * Returns a {@link LocalDate} of this year/month and the given day of the month.
     *
     * @param self      the day of month
     * @param yearMonth a year-month
     * @return a LocalDate
     */
    public static LocalDate or(final Integer self, final YearMonth yearMonth) {
        return yearMonth.atDay(self);
    }

}
