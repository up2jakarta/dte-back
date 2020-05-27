package io.github.up2jakarta.dte.web.converters;


import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import io.github.up2jakarta.dte.web.exceptions.EnumProcessingException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

/**
 * Generic enum JSON Deserializer.
 *
 * @param <T> the enum class
 */
public class EnumDeserializer<T extends Enum> extends StdDeserializer<T> {

    private static final String MSG_FORMAT = "invalid %s at L%d:C%d (must be one of {%s})";
    private final T[] constants;

    /**
     * Public constructor for EnumDeserializer.
     *
     * @param enumClass the enum class
     */
    public EnumDeserializer(final Class<T> enumClass) {
        super(enumClass);
        this.constants = enumClass.getEnumConstants();
    }

    @Override
    public T deserialize(final JsonParser parser, final DeserializationContext context) throws IOException {
        final String value = parser.getText();
        if (value.isEmpty()) {
            return null;
        }
        for (final T constant : constants) {
            if (constant.name().equals(value)) {
                return constant;
            }
        }
        final String origin = Optional.ofNullable(parser.getCurrentName()).orElse(_valueClass.getCanonicalName());
        final String field = Optional.ofNullable(parser.getCurrentName()).orElse("enum");
        throw new EnumProcessingException(buildMessage(field, parser.getCurrentLocation()), origin);
    }

    private String buildMessage(final String field, final JsonLocation loc) {
        return String.format(MSG_FORMAT, field, loc.getLineNr(), loc.getColumnNr(), Arrays.toString(constants));
    }
}
