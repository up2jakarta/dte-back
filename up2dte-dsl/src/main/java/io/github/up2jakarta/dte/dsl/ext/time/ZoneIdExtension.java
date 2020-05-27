package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;
import java.time.format.TextStyle;
import java.util.Locale;
import java.util.TimeZone;

/**
 * {@link ZoneId} extension methods.
 */
@SuppressWarnings({"unused", "WeakerAccess"})
public final class ZoneIdExtension {

    private ZoneIdExtension() {
    }

    /**
     * Returns a {@link java.time.ZonedDateTime} of this zone and the given {@link java.time.LocalDateTime}.
     *
     * @param self     a ZoneId
     * @param dateTime a LocalDateTime
     * @return a ZonedDateTime
     */
    public static ZonedDateTime or(final ZoneId self, final LocalDateTime dateTime) {
        return ZonedDateTime.of(dateTime, self);
    }

    /**
     * Combines the self {@code OffsetDateTime} with the given {@code self} to create one temporal has the same instant.
     *
     * @param self     a ZoneId
     * @param dateTime an OffsetDateTime
     * @return an ZonedDateTime
     */
    public static ZonedDateTime or(final ZoneId self, final OffsetDateTime dateTime) {
        return dateTime.atZoneSameInstant(self);
    }

    /**
     * Combines the self {@code ZonedDateTime} with the given {@code self} to create one temporal has the same instant.
     *
     * @param self     a ZoneOffset
     * @param dateTime an Instant
     * @return an OffsetDateTime
     */
    public static ZonedDateTime or(final ZoneId self, final Instant dateTime) {
        return dateTime.atZone(self);
    }

    /**
     * Gets the {@code TimeZone} for the given {@code zoneId}.
     *
     * @param self a {@link ZoneId} from which the time zone ID is obtained
     * @return the specified {@code TimeZone}, or the GMT zone if the given ID cannot be understood.
     */
    public static TimeZone toTimeZone(final ZoneId self) {
        return TimeZone.getTimeZone(self);
    }

    /**
     * Returns the name of this zone formatted according to the {@link java.time.format.TextStyle#FULL} text style.
     *
     * @param self a ZoneId
     * @return the full display name of the ZoneId
     */
    public static String getFullName(final ZoneId self) {
        return fullName(self, Locale.getDefault());
    }

    /**
     * Returns the name of this zone formatted according to the {@link java.time.format.TextStyle#FULL} text style
     * for the provided {@link java.util.Locale}.
     *
     * @param self   a ZoneId
     * @param locale a Locale
     * @return the full display name of the ZoneId
     */
    public static String fullName(final ZoneId self, final Locale locale) {
        return self.getDisplayName(TextStyle.FULL, locale);
    }

    /**
     * Returns the name of this zone formatted according to the {@link java.time.format.TextStyle#SHORT} text style.
     *
     * @param self a ZoneId
     * @return the short display name of the ZoneId
     */
    public static String getShortName(final ZoneId self) {
        return shortName(self, Locale.getDefault());
    }

    /**
     * Returns the name of this zone formatted according to the {@link java.time.format.TextStyle#SHORT} text style
     * for the provided {@link java.util.Locale}.
     *
     * @param self   a ZoneId
     * @param locale a Locale
     * @return the short display name of the ZoneId
     */
    public static String shortName(final ZoneId self, final Locale locale) {
        return self.getDisplayName(TextStyle.SHORT, locale);
    }

    /**
     * Returns a {@link java.time.ZoneOffset} for this zone as of now.
     *
     * @param self a ZoneId
     * @return a ZoneOffset
     */
    public static ZoneOffset getOffset(final ZoneId self) {
        return offset(self, Instant.now());
    }

    /**
     * Returns a {@link java.time.ZoneOffset} for this zone as of the provided {@link java.time.Instant}.
     *
     * @param self    a ZoneId
     * @param instant an Instant
     * @return a ZoneOffset
     */
    public static ZoneOffset offset(final ZoneId self, final Instant instant) {
        return self.getRules().getOffset(instant);
    }
}
