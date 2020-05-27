package io.github.up2jakarta.dte.dbm;

import org.springframework.boot.Banner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

import javax.sql.DataSource;

/**
 * Application Entry Point to launch Database Migration
 *
 * @author A.ABBESSI
 */
@SpringBootApplication
@PropertySource({"classpath:dbc.properties", "classpath:dbm.properties"})
public class MigrationApplication {

    /**
     * Main entry point.
     *
     * @param args the command line arguments
     */
    public static void main(final String[] args) {
        final SpringApplication app = new SpringApplication(MigrationApplication.class);
        app.setBannerMode(Banner.Mode.CONSOLE);
        app.run(args);
    }

    /**
     * @return the well configured DTE {@link DataSource}
     */
    @Bean
    @SuppressWarnings("unused")
    @ConfigurationProperties(prefix = "spring.datasource.tomcat")
    public DataSource defaultDataSource() {
        return DataSourceBuilder.create().build();
    }

}