package io.github.up2jakarta.dte.jpa.converters;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;
import java.time.Period;

/**
 * {@link Period} JPA Converter.
 */
@Converter
public class PeriodConverter implements AttributeConverter<Period, String> {

    @Override
    public String convertToDatabaseColumn(final Period attribute) {
        return attribute.toString();
    }

    @Override
    public Period convertToEntityAttribute(final String data) {
        return Period.parse(data);
    }
}
