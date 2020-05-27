package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;

/**
 * {@link ZoneId} extension methods.
 */
@SuppressWarnings({"unused"})
public final class ZoneOffsetExtension {

    private static final int MINUTES_PER_HOUR = 60;
    private static final int SECONDS_PER_MINUTE = 60;
    private static final int SECONDS_PER_HOUR = SECONDS_PER_MINUTE * MINUTES_PER_HOUR;

    private ZoneOffsetExtension() {
    }

    /**
     * Returns a {@code Duration} representing the duration between the {@code other} and {@code self}.
     *
     * @param other the start zone-offset, inclusive, not null
     * @param self  the end zone-offset, exclusive, not null
     * @return a {@code Duration}, not null
     */
    public static Duration minus(final ZoneOffset self, final ZoneOffset other) {
        return Duration.ofSeconds(other.getTotalSeconds() - self.getTotalSeconds());
    }

    /**
     * Returns the hours component of this offset. If the offset's total seconds are negative, a negative
     * value will be returned.
     *
     * @param self a ZoneOffset
     * @return the hours component value
     */
    public static int getHours(final ZoneOffset self) {
        final int totalSeconds = self.getTotalSeconds();
        final int absTotalSeconds = Math.abs(totalSeconds);
        final int absHours = absTotalSeconds / SECONDS_PER_HOUR;
        return totalSeconds < 0 ? -absHours : absHours;
    }

    /**
     * Returns the minutes component of this offset. If the offset's total seconds are negative, a negative
     * value will be returned.
     *
     * @param self a ZoneOffset
     * @return the minutes component value
     */
    public static int getMinutes(final ZoneOffset self) {
        final int totalSeconds = self.getTotalSeconds();
        final int absTotalSeconds = Math.abs(totalSeconds);
        final int absMinutes = (absTotalSeconds / SECONDS_PER_MINUTE) % MINUTES_PER_HOUR;
        return totalSeconds < 0 ? -absMinutes : absMinutes;
    }

    /**
     * Returns the seconds component of this offset. This is not the same as the total seconds. For example:
     * <pre>
     *     def offset = ZoneOffset.ofHoursMinutesSeconds(0, 1, 1)
     *     assert offset.seconds == 1
     *     assert offset.totalSeconds == 61
     * </pre>
     * <p>
     * If the offset's total seconds are negative, a negative value will be returned.
     *
     * @param self a ZoneOffset
     * @return the seconds component value
     */
    public static int getSeconds(final ZoneOffset self) {
        final int totalSeconds = self.getTotalSeconds();
        final int absTotalSeconds = Math.abs(totalSeconds);
        final int absSeconds = absTotalSeconds % SECONDS_PER_MINUTE;
        return totalSeconds < 0 ? -absSeconds : absSeconds;
    }

    /**
     * Combines the self {@code OffsetDateTime} with the given {@code self} to create one temporal has the same instant.
     *
     * @param self     a ZoneOffset
     * @param dateTime an Instant
     * @return an OffsetDateTime
     */
    public static OffsetDateTime or(final ZoneOffset self, final Instant dateTime) {
        return dateTime.atOffset(self);
    }

    /**
     * Returns an {@link OffsetDateTime} of this offset and the provided {@link LocalDateTime}.
     *
     * @param self     a ZoneOffset
     * @param dateTime a LocalDateTime
     * @return an OffsetDateTime
     */
    public static OffsetDateTime or(final ZoneOffset self, final LocalDateTime dateTime) {
        return OffsetDateTime.of(dateTime, self);
    }

    /**
     * Returns an {@link OffsetDateTime} of this offset and the provided {@link LocalTime}.
     *
     * @param self a ZoneOffset
     * @param time a LocalTime
     * @return an OffsetTime
     */
    public static OffsetTime or(final ZoneOffset self, final LocalTime time) {
        return OffsetTime.of(time, self);
    }

}
