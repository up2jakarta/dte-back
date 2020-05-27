package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.Duration;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.OffsetTime;

/**
 * {@link OffsetDateTime} extension methods.
 */
@SuppressWarnings({"unused"})
public final class OffsetTimeExtension {

    private OffsetTimeExtension() {
    }

    /**
     * Returns an {@link OffsetTime} that is {@code seconds} seconds after this time.
     *
     * @param self    an OffsetTime
     * @param seconds the number of seconds to add
     * @return an OffsetTime
     */
    public static OffsetTime plus(final OffsetTime self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns an {@link OffsetTime} that is {@code seconds} seconds before this time.
     *
     * @param self    an OffsetTime
     * @param seconds the number of seconds to subtract
     * @return an OffsetTime
     */
    public static OffsetTime minus(final OffsetTime self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start instant, inclusive, not null
     * @param self  the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final OffsetTime self, final OffsetTime other) {
        return Duration.between(other, self);
    }

    /**
     * Returns an {@link OffsetTime} that is one second after this time.
     *
     * @param self an OffsetTime
     * @return an OffsetTime
     */
    public static OffsetTime next(final OffsetTime self) {
        return plus(self, 1);
    }

    /**
     * Returns an {@link OffsetTime} that is one second before this time.
     *
     * @param self an OffsetTime
     * @return an OffsetTime
     */
    public static OffsetTime previous(final OffsetTime self) {
        return minus(self, 1);
    }

    /**
     * Returns an {@link java.time.OffsetDateTime} of this time and the provided {@link java.time.LocalDate}.
     *
     * @param self an OffsetTime
     * @param date a LocalDate
     * @return an OffsetDateTime
     */
    public static OffsetDateTime or(final OffsetTime self, final LocalDate date) {
        return OffsetDateTime.of(date, self.toLocalTime(), self.getOffset());
    }
}
