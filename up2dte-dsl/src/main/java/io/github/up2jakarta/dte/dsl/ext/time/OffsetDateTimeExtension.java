package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

/**
 * {@link OffsetDateTime} extension methods.
 */
@SuppressWarnings({"unused"})
public final class OffsetDateTimeExtension {

    private OffsetDateTimeExtension() {
    }

    /**
     * Returns an {@link java.time.OffsetDateTime} that is {@code seconds} seconds after this date/time.
     *
     * @param self    an OffsetDateTime
     * @param seconds the number of seconds to add
     * @return an OffsetDateTime
     */
    public static OffsetDateTime plus(final OffsetDateTime self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns an {@link java.time.OffsetDateTime} that is {@code seconds} seconds before this date/time.
     *
     * @param self    an OffsetDateTime
     * @param seconds the number of seconds to subtract
     * @return an OffsetDateTime
     */
    public static OffsetDateTime minus(final OffsetDateTime self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start instant, inclusive, not null
     * @param self  the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final OffsetDateTime self, final OffsetDateTime other) {
        return Duration.between(other, self);
    }

    /**
     * Returns an {@link java.time.OffsetDateTime} one second after this date/time.
     *
     * @param self an OffsetDateTime
     * @return an OffsetDateTime
     */
    public static OffsetDateTime next(final OffsetDateTime self) {
        return plus(self, 1);
    }

    /**
     * Returns an {@link java.time.OffsetDateTime} one second before this date/time.
     *
     * @param self an OffsetDateTime
     * @return an OffsetDateTime
     */
    public static OffsetDateTime previous(final OffsetDateTime self) {
        return minus(self, 1);
    }

    /**
     * Combines the self {@code OffsetDateTime} with the given {@code zone} to create one temporal has the same instant.
     *
     * @param self an {@code OffsetDateTime}
     * @param zone a the zone to use, such as {@code Europe/Paris}
     * @return an OffsetTime
     */
    public static ZonedDateTime or(final OffsetDateTime self, final ZoneId zone) {
        return self.atZoneSameInstant(zone);
    }

    /**
     * Converts the {@code self} to an {@code Instant} representing the same point on the time-line as this date-time.
     *
     * @param self an {@code OffsetDateTime}
     * @return an {@code Instant} representing the same instant, not null
     */
    public static Instant bitwiseNegate(final OffsetDateTime self) {
        return self.toInstant();
    }

}
