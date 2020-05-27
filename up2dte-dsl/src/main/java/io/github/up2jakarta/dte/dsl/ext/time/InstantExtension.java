package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

/**
 * {@link Instant} extension methods.
 */
@SuppressWarnings("unused")
public final class InstantExtension {

    private InstantExtension() {
    }

    /**
     * Returns an {@link java.time.Instant} that is {@code seconds} seconds after this instant.
     *
     * @param self    an Instant
     * @param seconds the number of seconds to add
     * @return an Instant
     */
    public static Instant plus(final Instant self, final long seconds) {
        return self.plusSeconds(seconds);
    }

    /**
     * Returns an {@link java.time.Instant} that is {@code seconds} seconds before this instant.
     *
     * @param self    an Instant
     * @param seconds the number of seconds to subtract
     * @return an Instant
     */
    public static Instant minus(final Instant self, final long seconds) {
        return self.minusSeconds(seconds);
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start instant, inclusive, not null
     * @param self  the end instant, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final Instant self, final Instant other) {
        return Duration.between(other, self);
    }

    /**
     * Returns an {@link java.time.Instant} that is one second after this instant.
     *
     * @param self an Instant
     * @return an Instant one second ahead
     */
    public static Instant next(final Instant self) {
        return plus(self, 1);
    }

    /**
     * Returns an {@link java.time.Instant} that one second before this instant.
     *
     * @param self an Instant
     * @return an Instant one second behind
     */
    public static Instant previous(final Instant self) {
        return minus(self, 1);
    }

    /**
     * Combines the self {@code OffsetDateTime} with the given {@code zone} to create one temporal has the same instant.
     *
     * @param self   an {@code OffsetDateTime}
     * @param offset a the zone-offset to use
     * @return an OffsetTime
     */
    public static OffsetDateTime or(final Instant self, final ZoneOffset offset) {
        return self.atOffset(offset);
    }

    /**
     * Combines the self {@code OffsetDateTime} with the given {@code zone} to create one temporal has the same instant.
     *
     * @param self an {@code OffsetDateTime}
     * @param zone a the zone to use, such as {@code Europe/Paris}
     * @return an OffsetTime
     */
    public static ZonedDateTime or(final Instant self, final ZoneId zone) {
        return self.atZone(zone);
    }

}
