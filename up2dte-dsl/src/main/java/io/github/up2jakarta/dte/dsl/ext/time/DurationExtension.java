package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

/**
 * {@link Duration} extension methods.
 */
@SuppressWarnings("unused")
public final class DurationExtension {

    private DurationExtension() {
    }

    /**
     * Supports the unary minus operator; equivalent to calling the {@link Duration#negated()} method.
     *
     * @param self a Duration
     * @return a Duration
     */
    public static Duration negative(final Duration self) {
        return self.negated();
    }

    /**
     * Supports the unary plus operator; equivalent to calling the {@link Duration#abs()} method.
     *
     * @param self a Duration
     * @return a Duration
     */
    public static Duration positive(final Duration self) {
        return self.abs();
    }

    /**
     * Returns a {@link java.time.Duration} that is {@code seconds} seconds longer than this duration.
     *
     * @param self    a Duration
     * @param seconds the number of seconds to add
     * @return a Duration
     */
    public static Duration plus(final Duration self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns a copy of the provided {@code time} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param time the the specified {@code LocalTime}
     * @return a {@code LocalDateTime} based on this time with the addition made
     */
    public static LocalTime plus(final Duration self, final LocalTime time) {
        return time.plus(self);
    }

    /**
     * Returns a copy of the provided {@code date} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param time the the specified {@code OffsetTime}
     * @return a {@code OffsetTime} based on this date-time with the addition made
     */
    public static OffsetTime plus(final Duration self, final OffsetTime time) {
        return time.plus(self);
    }

    /**
     * Returns a copy of the provided {@code date} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param date the the specified {@code LocalDateTime}
     * @return a {@code LocalDateTime} based on this date-time with the addition made
     */
    public static LocalDateTime plus(final Duration self, final LocalDateTime date) {
        return date.plus(self);
    }

    /**
     * Returns a copy of the provided {@code date} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param date the the specified {@code ZonedDateTime}
     * @return a {@code ZonedDateTime} based on this date-time with the addition made
     */
    public static ZonedDateTime plus(final Duration self, final ZonedDateTime date) {
        return date.plus(self);
    }

    /**
     * Returns a copy of the provided {@code date} with the specified {@code self} added.
     *
     * @param self the current {@code Duration} to add
     * @param date the the specified {@code OffsetDateTime}
     * @return a {@code OffsetDateTime} based on this date-time with the addition made
     */
    public static OffsetDateTime plus(final Duration self, final OffsetDateTime date) {
        return date.plus(self);
    }

    /**
     * Returns a {@link java.time.Duration} that is {@code seconds} seconds shorter that this duration.
     *
     * @param self    a Duration
     * @param seconds the number of seconds to subtract
     * @return a Duration
     */
    public static Duration minus(final Duration self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@link java.time.Duration} that is one second longer than this duration.
     *
     * @param self a Duration
     * @return a Duration
     */
    public static Duration next(final Duration self) {
        return self.plusSeconds(1);
    }

    /**
     * Returns a {@link java.time.Duration} that is one second shorter than this duration.
     *
     * @param self a Duration
     * @return a Duration
     */
    public static Duration previous(final Duration self) {
        return self.minusSeconds(1);
    }

    /**
     * Supports the multiplication operator; equivalent to calling the {@link Duration#multipliedBy(long)} method.
     *
     * @param self   a Duration
     * @param scalar the value to multiply by
     * @return a Duration
     */
    public static Duration multiply(final Duration self, final long scalar) {
        return self.multipliedBy(scalar);
    }

    /**
     * Supports the division operator; equivalent to calling the {@link Duration#dividedBy(long)} method.
     *
     * @param self   a Duration
     * @param scalar the value to divide by
     * @return a Duration
     */
    public static Duration div(final Duration self, final long scalar) {
        return self.dividedBy(scalar);
    }

}
