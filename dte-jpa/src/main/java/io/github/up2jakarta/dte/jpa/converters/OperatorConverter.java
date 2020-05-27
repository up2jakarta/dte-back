package io.github.up2jakarta.dte.jpa.converters;

import io.github.up2jakarta.dte.exe.api.Operator;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

/**
 * {@link Operator} JPA Converter.
 */
@Converter(autoApply = true)
public class OperatorConverter implements AttributeConverter<Operator, Character> {

    @Override
    public Character convertToDatabaseColumn(final Operator status) {
        if (status == Operator.OR) {
            return 'O';
        }
        if (status == Operator.XOR) {
            return 'X';
        }
        return 'A';
    }

    @Override
    public Operator convertToEntityAttribute(final Character code) {
        if (code == 'O') {
            return Operator.OR;
        }
        if (code == 'X') {
            return Operator.XOR;
        }
        return Operator.AND;
    }
}

