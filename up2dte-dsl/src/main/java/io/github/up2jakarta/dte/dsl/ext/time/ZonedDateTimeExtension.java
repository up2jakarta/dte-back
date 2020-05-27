package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.Duration;
import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * {@link ZonedDateTime} extension methods.
 */
@SuppressWarnings({"unused"})
public final class ZonedDateTimeExtension {

    private ZonedDateTimeExtension() {
    }

    /**
     * Returns a {@link java.time.ZonedDateTime} that is {@code seconds} seconds after this date/time.
     *
     * @param self    an ZonedDateTime
     * @param seconds the number of seconds to add
     * @return a ZonedDateTime
     */
    public static ZonedDateTime plus(final ZonedDateTime self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns a {@link java.time.ZonedDateTime} that is {@code seconds} seconds before this date/time.
     *
     * @param self    a ZonedDateTime
     * @param seconds the number of seconds to subtract
     * @return a ZonedDateTime
     */
    public static ZonedDateTime minus(final ZonedDateTime self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start instant, inclusive, not null
     * @param self  the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final ZonedDateTime self, final ZonedDateTime other) {
        return Duration.between(other, self);
    }

    /**
     * Returns a {@link java.time.ZonedDateTime} that is one second after this date/time.
     *
     * @param self a ZonedDateTime
     * @return a ZonedDateTime
     */
    public static ZonedDateTime next(final ZonedDateTime self) {
        return plus(self, 1);
    }

    /**
     * Returns a {@link java.time.ZonedDateTime} that is one second before this date/time.
     *
     * @param self a ZonedDateTime
     * @return a ZonedDateTime
     */
    public static ZonedDateTime previous(final ZonedDateTime self) {
        return minus(self, 1);
    }

    /**
     * Converts the {@code self} to an {@code Instant} representing the same point on the time-line as this date-time.
     *
     * @param self an {@code OffsetDateTime}
     * @return an {@code Instant} representing the same instant, not null
     */
    public static Instant bitwiseNegate(final ZonedDateTime self) {
        return self.toInstant();
    }

}
