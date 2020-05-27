package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

/**
 * {@link LocalTime} extension methods.
 */
@SuppressWarnings("unused")
public final class LocalTimeExtension {

    private LocalTimeExtension() {
    }

    /**
     * Returns a {@link java.time.LocalTime} that is {@code seconds} seconds after this time.
     *
     * @param self    a LocalTime
     * @param seconds the number of seconds to add
     * @return a LocalTime
     */
    public static LocalTime plus(final LocalTime self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns a {@link java.time.LocalTime} that is {@code seconds} seconds before this time.
     *
     * @param self    a LocalTime
     * @param seconds the number of seconds to subtract
     * @return a LocalTime
     */
    public static LocalTime minus(final LocalTime self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start instant, inclusive, not null
     * @param self  the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final LocalTime self, final LocalTime other) {
        return Duration.between(other, self);
    }

    /**
     * Returns a {@link java.time.LocalTime} that is one second after this time.
     *
     * @param self a LocalTime
     * @return a LocalTime
     */
    public static LocalTime next(final LocalTime self) {
        return plus(self, 1);
    }

    /**
     * Returns a {@link java.time.LocalTime} that is one second before this time.
     *
     * @param self a LocalTime
     * @return a LocalTime
     */
    public static LocalTime previous(final LocalTime self) {
        return minus(self, 1);
    }

    /**
     * Returns a {@link java.time.LocalDateTime} of this time and the provided {@link java.time.LocalDate}.
     *
     * @param self a LocalTime
     * @param date a LocalDate
     * @return a LocalDateTime
     */
    public static LocalDateTime or(final LocalTime self, final LocalDate date) {
        return LocalDateTime.of(date, self);
    }

    /**
     * Returns an {@link java.time.OffsetTime} of this time and the provided {@link java.time.ZoneOffset}.
     *
     * @param self   a LocalTime
     * @param offset a ZoneOffset
     * @return an OffsetTime
     */
    public static OffsetTime or(final LocalTime self, final ZoneOffset offset) {
        return OffsetTime.of(self, offset);
    }

}
