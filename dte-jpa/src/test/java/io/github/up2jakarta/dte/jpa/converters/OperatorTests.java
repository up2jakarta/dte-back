package io.github.up2jakarta.dte.jpa.converters;

import org.junit.Assert;
import org.junit.Test;
import io.github.up2jakarta.dte.exe.api.Operator;

public class OperatorTests {

    private final OperatorConverter converter = new OperatorConverter();

    @Test
    public void shouldDeserializeXor() {
        Assert.assertSame(Operator.XOR, converter.convertToEntityAttribute('X'));
    }

    @Test
    public void shouldDeserializeAnd() {
        Assert.assertSame(Operator.AND, converter.convertToEntityAttribute('A'));
    }

    @Test
    public void shouldDeserializeOr() {
        Assert.assertSame(Operator.OR, converter.convertToEntityAttribute('O'));
    }

    @Test
    public void shouldSerializeXor() {
        Assert.assertSame('X', converter.convertToDatabaseColumn(Operator.XOR));
    }

    @Test
    public void shouldSerializeAnd() {
        Assert.assertSame('A', converter.convertToDatabaseColumn(Operator.AND));
    }

    @Test
    public void shouldSerializeOr() {
        Assert.assertSame('O', converter.convertToDatabaseColumn(Operator.OR));
    }
}
