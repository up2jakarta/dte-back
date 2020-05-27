package io.github.up2jakarta.dte.web.config;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.MapperFeature;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalTimeSerializer;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.validation.ValidationAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;
import io.github.up2jakarta.dte.exe.api.Operator;
import io.github.up2jakarta.dte.jpa.models.Computer;
import io.github.up2jakarta.dte.jpa.models.Decider;
import io.github.up2jakarta.dte.web.controllers.GroupController;
import io.github.up2jakarta.dte.web.converters.EnumDeserializer;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static com.fasterxml.jackson.core.util.VersionUtil.parseVersion;
import static java.time.format.DateTimeFormatter.*;
import static io.github.up2jakarta.dte.exe.StaticEngine.version;

/**
 * Rest configuration.
 */
@Configuration
@ComponentScan(basePackageClasses = {GroupController.class})
@ImportAutoConfiguration(ValidationAutoConfiguration.class)
public class RestConfiguration {

    private static final Version VERSION = parseVersion(version().getBuildVersion(), "io.github.up2jakarta.dte", "WEB");

    /**
     * @return builder used to create {@link com.fasterxml.jackson.databind.ObjectMapper}.
     */
    @Bean
    @Primary
    Jackson2ObjectMapperBuilder jacksonMapperBuilder() {
        final Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder();
        // Date and Time Module
        final JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(ISO_LOCAL_DATE));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(ISO_LOCAL_DATE));
        // TODO write the following as OffsetXXX (idea Instant) + add Date + calendar OR see how in front
        javaTimeModule.addSerializer(LocalDateTime.class, new LocalDateTimeSerializer(ISO_LOCAL_DATE_TIME));
        javaTimeModule.addDeserializer(LocalDateTime.class, new LocalDateTimeDeserializer(ISO_LOCAL_DATE_TIME));
        javaTimeModule.addSerializer(LocalTime.class, new LocalTimeSerializer(ISO_LOCAL_TIME));
        javaTimeModule.addDeserializer(LocalTime.class, new LocalTimeDeserializer(ISO_LOCAL_TIME));
        // OffsetDateTime OffsetTime ZonedDateTime

        builder.featuresToDisable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        builder.featuresToDisable(SerializationFeature.WRITE_DURATIONS_AS_TIMESTAMPS);
        builder.featuresToDisable(SerializationFeature.WRITE_DATES_WITH_ZONE_ID);
        // Enum Module
        final SimpleModule enumModule = new SimpleModule("EnumModule", VERSION);
        enumModule.addDeserializer(Operator.class, new EnumDeserializer<>(Operator.class));
        enumModule.addDeserializer(Decider.Type.class, new EnumDeserializer<>(Decider.Type.class));
        enumModule.addDeserializer(Computer.Type.class, new EnumDeserializer<>(Computer.Type.class));

        builder.modules(javaTimeModule, enumModule);
        // Visibility
        builder.visibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.NON_PRIVATE);
        builder.visibility(PropertyAccessor.GETTER, JsonAutoDetect.Visibility.NON_PRIVATE);
        builder.visibility(PropertyAccessor.IS_GETTER, JsonAutoDetect.Visibility.NON_PRIVATE);
        builder.visibility(PropertyAccessor.SETTER, JsonAutoDetect.Visibility.NON_PRIVATE);
        builder.visibility(PropertyAccessor.CREATOR, JsonAutoDetect.Visibility.NON_PRIVATE);

        // Printer & Order
        builder.featuresToEnable(MapperFeature.SORT_PROPERTIES_ALPHABETICALLY);
        return builder;
    }

}
