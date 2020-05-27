package io.github.up2jakarta.dte.dsl.ext.time;

import java.time.*;
import java.time.format.DateTimeFormatter;

/**
 * This class defines new static extension methods which appear on normal JDK
 * Date/Time API (java.time) classes inside the Groovy environment.
 * <p>
 * source org.apache.groovy.datetime.extensions.DateTimeStaticExtensions
 */
@SuppressWarnings("unused")
public final class StaticParserExtension {

    private StaticParserExtension() {
    }

    /**
     * Parse text into a {@link java.time.LocalDateTime} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a LocalDateTime representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.LocalDateTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static LocalDateTime parse(final LocalDateTime type, final CharSequence text) {
        return LocalDateTime.parse(text, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
    }

    /**
     * Parse text into a {@link java.time.LocalDate} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a LocalDate representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.LocalDate#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static LocalDate parse(final LocalDate type, final CharSequence text) {
        return LocalDate.parse(text, DateTimeFormatter.ISO_LOCAL_DATE);
    }

    /**
     * Parse text into a {@link java.time.LocalTime} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a LocalTime representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.LocalTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static LocalTime parse(final LocalTime type, final CharSequence text) {
        return LocalTime.parse(text, DateTimeFormatter.ISO_LOCAL_TIME);
    }

    /**
     * Parse text into an {@link java.time.OffsetDateTime} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return an OffsetDateTime representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.OffsetDateTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static OffsetDateTime parse(final OffsetDateTime type, final CharSequence text) {
        return OffsetDateTime.parse(text, DateTimeFormatter.ISO_OFFSET_DATE_TIME);
    }

    /**
     * Parse text into an {@link java.time.OffsetTime} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return an OffsetTime representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.OffsetTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static OffsetTime parse(final OffsetTime type, final CharSequence text) {
        return OffsetTime.parse(text, DateTimeFormatter.ISO_OFFSET_TIME);
    }

    /**
     * Parse text into a {@link java.time.ZonedDateTime} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a ZonedDateTime representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.ZonedDateTime#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static ZonedDateTime parse(final ZonedDateTime type, final CharSequence text) {
        return ZonedDateTime.parse(text, DateTimeFormatter.ISO_ZONED_DATE_TIME);
    }

    /**
     * Parse text into a {@link java.time.Year} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a Year representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.Year#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static Year parse(final Year type, final CharSequence text) {
        return Year.parse(text, DateTimeFormatter.ofPattern("yyyy"));
    }

    /**
     * Parse text into a {@link java.time.YearMonth} using the provided pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a YearMonth representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.YearMonth#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static YearMonth parse(final YearMonth type, final CharSequence text) {
        return YearMonth.parse(text, DateTimeFormatter.ofPattern("yyyy-MM"));
    }

    /**
     * Parse text into a {@link java.time.MonthDay} using the ISO pattern.
     *
     * @param type placeholder variable used by Groovy categories; ignored for default static methods
     * @param text String to be parsed to create the date instance
     * @return a MonthDay representing the parsed text
     * @throws java.lang.IllegalArgumentException      if the pattern is invalid
     * @throws java.time.format.DateTimeParseException if the text cannot be parsed
     * @see java.time.format.DateTimeFormatter
     * @see java.time.MonthDay#parse(java.lang.CharSequence, java.time.format.DateTimeFormatter)
     */
    public static MonthDay parse(final MonthDay type, final CharSequence text) {
        return MonthDay.parse(text, DateTimeFormatter.ofPattern("MM-dd"));
    }
}
