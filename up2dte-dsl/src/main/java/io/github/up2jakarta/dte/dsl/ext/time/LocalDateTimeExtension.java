package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

/**
 * {@link LocalDateTime} extension methods.
 */
@SuppressWarnings("unused")
public final class LocalDateTimeExtension {

    private LocalDateTimeExtension() {
    }

    /**
     * Returns a copy of {@code self} with the specified {@code seconds} added.
     *
     * @param self    the current {@code LocalDateTime}
     * @param seconds the seconds to add
     * @return a {@code LocalDateTime} based on this date-time with the addition made
     */
    public static LocalDateTime plus(final LocalDateTime self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns a {@link java.time.LocalDateTime} that is {@code seconds} seconds before this date/time.
     *
     * @param self    a LocalDateTime
     * @param seconds the number of seconds to subtract
     * @return a LocalDateTime
     */
    public static LocalDateTime minus(final LocalDateTime self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start instant, inclusive, not null
     * @param self  the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final LocalDateTime self, final LocalDateTime other) {
        return Duration.between(other, self);
    }

    /**
     * Returns a {@link java.time.LocalDateTime} that is one second after this date/time.
     *
     * @param self a LocalDateTime
     * @return a LocalDateTime
     */
    public static LocalDateTime next(final LocalDateTime self) {
        return plus(self, 1);
    }

    /**
     * Returns a {@link java.time.LocalDateTime} that is one second before this date/time.
     *
     * @param self a LocalDateTime
     * @return a LocalDateTime
     */
    public static LocalDateTime previous(final LocalDateTime self) {
        return minus(self, 1);
    }

    /**
     * Returns an {@link java.time.OffsetDateTime} of this date/time and the provided {@link java.time.ZoneOffset}.
     *
     * @param self   a LocalDateTime
     * @param offset a ZoneOffset
     * @return an OffsetDateTime
     */
    public static OffsetDateTime or(final LocalDateTime self, final ZoneOffset offset) {
        return OffsetDateTime.of(self, offset);
    }

    /**
     * Returns a {@link java.time.OffsetDateTime} of this date/time and the provided {@link java.time.ZoneId}.
     *
     * @param self a LocalDateTime
     * @param zone a ZoneId
     * @return a ZonedDateTime
     */
    public static ZonedDateTime or(final LocalDateTime self, final ZoneId zone) {
        return ZonedDateTime.of(self, zone);
    }

    /**
     * Converts the {@code self} to an {@code Instant} representing the same point on the time-line as this date-time.
     *
     * @param self an {@code OffsetDateTime}
     * @return an {@code Instant} representing the same instant, not null
     */
    public static Instant bitwiseNegate(final LocalDateTime self) {
        return self.atZone(ZoneOffset.systemDefault()).toInstant();
    }
}
