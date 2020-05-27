package io.github.up2jakarta.dte.web.converters;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.web.TestConfiguration;
import io.github.up2jakarta.dte.web.exceptions.EnumProcessingException;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {TestConfiguration.class})
@TestPropertySource(properties = {
        "spring.datasource.data=classpath:groups.sql",
        "spring.datasource.tomcat.url=jdbc:h2:mem:enum_test;MODE=PostgreSQL;"
})
public class EnumDeserializerTests {

    @Autowired
    private ObjectMapper mapper;

    @Test
    public void shouldDeserializeOperator() throws Exception {
        // Given
        final String json = "\"AND\"";

        // When
        final Operator operator = mapper.readValue(json, Operator.class);

        // Then
        Assert.assertEquals(Operator.AND, operator);
    }

    @Test
    public void shouldDeserializeBTreeType() throws Exception {
        // Given
        final String json = "\"MIXED\"";

        // When
        final Decider.Type type = mapper.readValue(json, Decider.Type.class);

        // Then
        Assert.assertEquals(Decider.Type.MIXED, type);
    }

    @Test
    public void shouldDeserializeDTreeType() throws Exception {
        // Given
        final String json = "\"MIXED\"";

        // When
        final Computer.Type type = mapper.readValue(json, Computer.Type.class);

        // Then
        Assert.assertEquals(Computer.Type.MIXED, type);
    }

    @Test
    public void shouldDeserializeEmpty() throws Exception {
        // Given
        final String json = "\"\"";

        // When
        final Operator operator = mapper.readValue(json, Operator.class);

        // Then
        Assert.assertNull(operator);
    }

    @Test(expected = EnumProcessingException.class)
    public void shouldFailsWhenUnknown() throws Exception {
        // Given
        final String json = "\"TEST\"";

        // When
        mapper.readValue(json, Operator.class);
    }
}
