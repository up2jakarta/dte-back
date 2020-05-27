package io.github.up2jakarta.dte.web.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.*;
import io.github.up2jakarta.dte.jpa.entities.Group;
import io.github.up2jakarta.dte.jpa.repositories.GroupRepository;
import io.github.up2jakarta.dte.jpa.views.VDecider;

import javax.sql.DataSource;

/**
 * Persistence layer configuration.
 */
@Configuration
@ComponentScan(basePackageClasses = GroupRepository.class)
@EntityScan(basePackageClasses = {Group.class, VDecider.class})
@PropertySource(value = "classpath:dbc.properties", ignoreResourceNotFound = true)
public class PersistenceConfiguration {

    /**
     * @return the well configured DTE {@link DataSource}
     */
    @Bean
    @ConfigurationProperties(prefix = "spring.datasource.tomcat")
    @Primary
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }
}
